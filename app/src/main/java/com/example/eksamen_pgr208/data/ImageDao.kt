package com.example.eksamen_pgr208.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addImage(image: Image)

    @Query("SELECT * FROM image_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Image>>

}