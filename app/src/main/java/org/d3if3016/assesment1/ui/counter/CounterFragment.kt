package org.d3if3016.assesment1.ui.counter

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCounterBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        }
        return super.onOptionsItemSelected(item)
    }

    //  Count how many column that possible for device
    private fun calculateNoOfColumns(context: Context, columnWidthDp: Float = 180f): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}