package org.d3if3016.assesment1.ui.myData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3016.assesment1.data.VehiclesDao

class MyDataViewModelFactory(private val db: VehiclesDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyDataViewModel::class.java))
            return MyDataViewModel(db) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}