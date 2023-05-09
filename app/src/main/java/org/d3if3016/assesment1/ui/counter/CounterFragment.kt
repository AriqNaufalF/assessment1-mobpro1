package org.d3if3016.assesment1.ui.counter

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import org.d3if3016.assesment1.R
import org.d3if3016.assesment1.databinding.FragmentCounterBinding

class CounterFragment : Fragment() {
    private val viewModel: CounterViewModel by lazy {
        ViewModelProvider(requireActivity())[CounterViewModel::class.java]
    }

    private lateinit var binding: FragmentCounterBinding
    private lateinit var counterAdapter: CounterAdapter
    private lateinit var chronometer: Chronometer
    private lateinit var imageButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCounterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        chronometer = binding.chronometer
        imageButton = binding.imageButton
        counterAdapter = CounterAdapter(viewModel::updateVehicle)
        //        Set recycler view
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), calculateNoOfColumns(requireContext()))
        with(binding.recyclerView) {
            adapter = counterAdapter
            setHasFixedSize(true)
        }
        viewModel.getVehiclesData().observe(viewLifecycleOwner) {
            counterAdapter.submitList(it)
        }

        viewModel.getIsRunning().observe(viewLifecycleOwner) {
            if (viewModel.getElapsedTime() != 0L && it) {
                // Jika stopwatch masih berjalan dan konfigurasi diganti lanjutkan waktunya
                chronometer.base = viewModel.getElapsedTime()
                imageButton.setImageResource(R.drawable.pause)
                chronometer.start()
            } else if (viewModel.getElapsedTime() != 0L) {
                // Jika stopwatch tidak berjalan serta waktu tidak sama dengan 0 dan konfigurasi diganti kembalikan waktu semula
                chronometer.base = viewModel.getStartTime() + SystemClock.elapsedRealtime()
            }
        }

        imageButton.setOnClickListener {
            startStopTimer()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.counter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_my_data -> {
                // TODO: to my data fragment
                return true
            }
            R.id.refresh_menu -> {
                viewModel.resetData()
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.stop()
                imageButton.setImageResource(R.drawable.play_arrow)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //  Count how many column that possible for device
    private fun calculateNoOfColumns(context: Context, columnWidthDp: Float = 180f): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    private fun startStopTimer() {
        if (viewModel.getIsRunning().value!!) {
            // Update variable start time saat tombol pause ditekan
            viewModel.setStartTime(chronometer.base - SystemClock.elapsedRealtime())
            chronometer.stop()
            imageButton.setImageResource(R.drawable.play_arrow)
            viewModel.setIsRunning(false)
        } else {
            // Mendapatkan waktu yang sudah berlalu mulai dari start time
            val elapsedTime = SystemClock.elapsedRealtime() + viewModel.getStartTime()
            viewModel.setElapsedTime(elapsedTime)
            chronometer.base = elapsedTime
            imageButton.setImageResource(R.drawable.pause)
            chronometer.start()
            viewModel.setIsRunning(true)
        }
    }
}