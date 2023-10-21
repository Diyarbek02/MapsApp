package com.diyarbekdev.mapsappforexam.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.diyarbekdev.mapsappforexam.data.models.UserMap


@Dao
interface UserMapDao {

    @Query("SELECT * FROM usermap")
    suspend fun getAllUserMaps(): List<UserMap>


}