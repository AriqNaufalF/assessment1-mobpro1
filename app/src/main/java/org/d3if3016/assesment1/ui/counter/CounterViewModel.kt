package org.d3if3016.assesment1.ui.counter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if3016.assesment1.data.Vehicle
import org.d3if3016.assesment1.data.getVehicles

class CounterViewModel : ViewModel() {
    private val vehicles = MutableLiveData(getVehicles())

    fun updateVehicle(vehicle: Vehicle) {
        vehicles.value = vehicles.value?.let {
            val currentIndex = it.indexOfFirst { v -> vehicle.name == v.name }
            val newList = it.toMutableList()
            newList[currentIndex] = vehicle.copy(count = it[currentIndex].count + 1)
            return@let newList
        }
    }

    fun getVehiclesData() = vehicles
}