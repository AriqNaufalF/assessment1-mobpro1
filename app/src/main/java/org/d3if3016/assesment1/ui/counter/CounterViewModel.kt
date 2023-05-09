package org.d3if3016.assesment1.ui.counter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if3016.assesment1.data.Vehicle
import org.d3if3016.assesment1.data.getVehicles

class CounterViewModel : ViewModel() {
    private val vehicles = MutableLiveData(getVehicles())
    private var elapsedTime = 0L
    private var startTime = 0L
    private var isRunning = MutableLiveData(false)

    fun resetData() {
        vehicles.value = getVehicles()
        elapsedTime = 0L
        startTime = 0L
        isRunning.value = false
    }

    fun updateVehicle(vehicle: Vehicle) {
        vehicles.value = vehicles.value?.let {
            val currentIndex = it.indexOfFirst { v -> vehicle.name == v.name }
            val newList = it.toMutableList()
            newList[currentIndex] = vehicle.copy(count = it[currentIndex].count + 1)
            return@let newList
        }
    }

    fun setElapsedTime(elapsedTime: Long) {
        this.elapsedTime = elapsedTime
    }

    fun setIsRunning(isRun: Boolean) {
        isRunning.value = isRun
    }

    fun setStartTime(startTime: Long) {
        this.startTime = startTime
    }

    fun getVehiclesData() = vehicles
    fun getElapsedTime() = elapsedTime
    fun getStartTime() = startTime
    fun getIsRunning() = isRunning
}