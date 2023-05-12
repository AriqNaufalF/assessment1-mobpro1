package org.d3if3016.assesment1.ui.myData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3016.assesment1.data.Vehicles
import org.d3if3016.assesment1.data.VehiclesDao

class MyDataViewModel(private val db: VehiclesDao) : ViewModel() {
    val data = db.getAllVehicles()

    fun deleteData() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.deleteAll()
        }
    }

    fun undoDeletedData(vehiclesData: List<Vehicles>) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.insertAll(*vehiclesData.toTypedArray())
        }
    }

    fun deleteItem(vehicles: Vehicles) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.delete(vehicles)
        }
    }

    fun undoDeletedItem(vehicles: Vehicles) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.insert(vehicles)
        }
    }
}