package org.d3if3016.assesment1.data

import androidx.annotation.DrawableRes
import org.d3if3016.assesment1.R

data class Vehicle(
    val name: String,
    @DrawableRes val imgRes: Int,
    val count: Int = 0
)

fun getVehicles(): List<Vehicle> {
    return listOf(
        Vehicle("Bike", R.drawable.bike),
        Vehicle("Motorcycle", R.drawable.motorcycle),
        Vehicle("Car", R.drawable.car),
        Vehicle("Mini truck", R.drawable.mini_truck),
        Vehicle("Bus", R.drawable.bus),
        Vehicle("Truck", R.drawable.truck),
    )
}
