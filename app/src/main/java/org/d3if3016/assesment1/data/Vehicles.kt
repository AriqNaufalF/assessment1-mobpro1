package org.d3if3016.assesment1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicles(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Long = System.currentTimeMillis(),
    val bike: Int,
    val motorcycle: Int,
    val car: Int,
    val miniTruck: Int,
    val bus: Int,
    val truck: Int,
    val time: String,
)
