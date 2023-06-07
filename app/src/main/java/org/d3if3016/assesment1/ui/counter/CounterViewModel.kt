package org.d3if3016.assesment1.ui.counter

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3016.assesment1.R
import org.d3if3016.assesment1.data.Vehicle
import org.d3if3016.assesment1.data.Vehicles
import org.d3if3016.assesment1.data.VehiclesDao
import org.d3if3016.assesment1.network.ApiStatus
import org.d3if3016.assesment1.network.NotificationWorker
import org.d3if3016.assesment1.network.VehiclesApi
import java.util.concurrent.TimeUnit

class CounterViewModel(private val db: VehiclesDao) : ViewModel() {
    private val vehicles = MutableLiveData<List<Vehicle>>()
    private val status = MutableLiveData<ApiStatus>()
    private var elapsedTime = 0L
    private var startTime = 0L
    private var isRunning = MutableLiveData(false)

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                vehicles.postValue(VehiclesApi.service.getVehicles())
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.e("CounterViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }
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
        retrieveData()
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

    fun getStatus() = status

    fun scheduleNotification(app: Application) {
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(app).enqueueUniqueWork(
            NotificationWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}