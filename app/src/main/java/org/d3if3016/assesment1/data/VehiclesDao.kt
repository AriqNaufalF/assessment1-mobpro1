package org.d3if3016.assesment1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VehiclesDao {
    @Insert
    fun insert(vehicles: Vehicles)

    @Insert
    fun insertAll(vararg vehicles: Vehicles)

    @Query("SELECT * FROM vehicles ORDER BY id DESC")
    fun getAllVehicles(): LiveData<List<Vehicles>>

    @Delete
    fun delete(vehicles: Vehicles)

    @Query("DELETE FROM vehicles")
    fun deleteAll()
}