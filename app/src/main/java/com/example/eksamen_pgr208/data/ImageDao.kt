package com.example.eksamen_pgr208.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageDao {

    // OnConflictStrategy.REPLACE means that if the user adds
    // the same image multiple times, it will be replaced each time
    // so that the table in the database doesn't fill up with copies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage3(image: Image): Long

    @Query("SELECT * FROM image_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Image>>

    @Delete
    fun deleteImage(image: Image)

}