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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.d3if3016.assesment1.R
import org.d3if3016.assesment1.data.SettingDataStore
import org.d3if3016.assesment1.data.VehiclesDb
import org.d3if3016.assesment1.data.dataStore
import org.d3if3016.assesment1.databinding.FragmentCounterBinding
import org.d3if3016.assesment1.network.ApiStatus

class CounterFragment : Fragment() {
    private val layoutDataStore: SettingDataStore by lazy {
        SettingDataStore(requireContext().dataStore)
    }
    private val viewModel: CounterViewModel by lazy {
        val db = VehiclesDb.getInstance(requireContext())
        val factory = CounterViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[CounterViewModel::class.java]
    }

    private lateinit var binding: FragmentCounterBinding
    private lateinit var counterAdapter: CounterAdapter
    private lateinit var chronometer: Chronometer
    private lateinit var imageButton: ImageButton
    private var isGridLayout = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCounterBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        counterAdapter = CounterAdapter(viewModel::updateVehicle)
        with(binding.recyclerView) {
            adapter = counterAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) {
            isGridLayout = it
            setLayout()
            activity?.invalidateOptionsMenu()
        }

        chronometer = binding.chronometer
        imageButton = binding.imageButton

        viewModel.getVehiclesData().observe(viewLifecycleOwner) {
            counterAdapter.submitList(it)
        }

        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
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
        binding.saveButton.setOnClickListener {
            val result = viewModel.saveData(chronometer.text.toString())
            if (result == null) {
                resetAllState()
                return@setOnClickListener
            }
            Toast.makeText(context, result, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.counter_menu, menu)

        val menuItem = menu.findItem(R.id.switch_layout_action)
        setIconAndTitle(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_my_data -> {
                findNavController().navigate(R.id.action_counterFragment_to_myDataFragment)
                return true
            }

            R.id.menu_about -> {
                findNavController().navigate(R.id.action_counterFragment_to_aboutFragment)
                return true
            }

            R.id.switch_layout_action -> {
                lifecycleScope.launch {
                    layoutDataStore.saveLayout(requireContext(), !isGridLayout)
                }
                return true
            }

            R.id.refresh_menu -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.refresh_confirmation)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        resetAllState()
                    }
                    .setNegativeButton(R.string.no) { dialog, _ ->
                        dialog.cancel()
                    }.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> binding.progressBar.visibility = View.VISIBLE
            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
            }

            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
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

    private fun setLayout() {
        binding.recyclerView.layoutManager = if (isGridLayout)
            GridLayoutManager(context, calculateNoOfColumns(requireContext()))
        else
            LinearLayoutManager(context)
    }

    private fun setIconAndTitle(menuItem: MenuItem) {
        var iconId = R.drawable.view_list
        var stringId = R.string.list_layout
        if (!isGridLayout) {
            iconId = R.drawable.grid_view
            stringId = R.string.grid_layout
        }
        menuItem.icon = ContextCompat.getDrawable(requireContext(), iconId)
        menuItem.title = getString(stringId)
    }

    private fun resetAllState() {
        viewModel.resetData()
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.stop()
        imageButton.setImageResource(R.drawable.play_arrow)
    }
}