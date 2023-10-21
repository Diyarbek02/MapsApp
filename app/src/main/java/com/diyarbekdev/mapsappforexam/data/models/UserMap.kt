package com.diyarbekdev.mapsappforexam.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usermap")
data class UserMap(
    @PrimaryKey val id: Int,
    val title: String
)
