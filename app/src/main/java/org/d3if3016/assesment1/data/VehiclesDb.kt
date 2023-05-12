package org.d3if3016.assesment1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Vehicles::class], version = 1, exportSchema = false)
abstract class VehiclesDb : RoomDatabase() {
    abstract val dao: VehiclesDao

    companion object {
        @Volatile
        private var INSTANCE: VehiclesDb? = null

        fun getInstance(context: Context): VehiclesDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        VehiclesDb::class.java,
                        "vehicles.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}