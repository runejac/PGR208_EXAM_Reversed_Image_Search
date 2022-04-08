package com.example.eksamen_pgr208.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


// Alle filer under data-folderen er skrevet ved hjelp av denne videoen:
// https://www.youtube.com/watch?v=lwAvI3WDXBY
// anbefaler å se den

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addImage(image: Image)

    @Query("SELECT * FROM image_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Image>>

}