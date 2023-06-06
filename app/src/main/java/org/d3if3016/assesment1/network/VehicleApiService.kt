package org.d3if3016.assesment1.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3016.assesment1.data.Vehicle
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://gist.githubusercontent.com/AriqNaufalF/4b9ecd7143707601f2beeec5e5647afd/raw/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface VehicleApiService {
    @GET("vehicles.json")
    suspend fun getVehicles(): List<Vehicle>
}

object VehiclesApi {
    val service: VehicleApiService by lazy {
        retrofit.create(VehicleApiService::class.java)
    }
    fun getVehiclesUrl(name: String) = "$BASE_URL$name.png"
}

enum class ApiStatus {LOADING, SUCCESS, FAILED}