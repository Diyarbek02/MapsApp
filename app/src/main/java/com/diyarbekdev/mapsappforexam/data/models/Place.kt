package com.diyarbekdev.mapsappforexam.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")
data class Place(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val latitude: String,
    val longitude: String,
    val usermapid: Int
)
