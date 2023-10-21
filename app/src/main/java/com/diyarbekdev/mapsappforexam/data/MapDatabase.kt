package com.diyarbekdev.mapsappforexam.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.diyarbekdev.mapsappforexam.data.dao.PlaceDao
import com.diyarbekdev.mapsappforexam.data.dao.UserMapDao

abstract class MapDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: MapDatabase? = null
        private val LOCK = Any()


        fun getInstance(context: Context): MapDatabase{
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    context,
                    MapDatabase::class.java,
                    "map.db")
                    .createFromAsset("map.db")
                    .build()
                INSTANCE =db
                return db
            }
        }
    }

    abstract fun dao(): PlaceDao

    abstract fun secdao(): UserMapDao
}