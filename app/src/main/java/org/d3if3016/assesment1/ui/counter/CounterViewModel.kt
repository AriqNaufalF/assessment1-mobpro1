package org.d3if3016.assesment1.ui.counter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3016.assesment1.R
import org.d3if3016.assesment1.data.Vehicle
import org.d3if3016.assesment1.data.Vehicles
import org.d3if3016.assesment1.data.VehiclesDao
import org.d3if3016.assesment1.data.getVehicles

class CounterViewModel(private val db: VehiclesDao) : ViewModel() {
    private val vehicles = MutableLiveData(getVehicles())
    private var elapsedTime = 0L
    private var startTime = 0L
    private var isRunning = MutableLiveData(false)

    fun saveData(chronoTextVal: String): Int? {
        return vehicles.value?.let {
            val finalData = Vehicles(
                bike = it[0].count,
                motorcycle = it[1].count,
                car = it[2].count,
                miniTruck = it[3].count,
                bus = it[4].count,
                truck = it[5].count,
                time = chronoTextVal
            )
            if (elapsedTime != 0L) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        db.insert(finalData)
                    }
                }
                return@let null
            }
            R.string.stopwatch_invalid
        }
    }

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