package org.d3if3016.assesment1.ui.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3016.assesment1.data.VehiclesDao

class CounterViewModelFactory(private val db: VehiclesDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CounterViewModel::class.java))
            return CounterViewModel(db) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}