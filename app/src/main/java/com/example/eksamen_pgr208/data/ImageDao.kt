package com.example.eksamen_pgr208.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    // OnConflictStrategy.REPLACE means that if the user adds
    // the same image multiple times, it will be replaced each time
    // so that the table in the database doesn't fill up with copies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage(image: Image)

    @Query("SELECT * FROM image_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Image>>

    // Using query annotation instead of delete because we are deleting the
    // image based on the image_link, and not passing the entire entity as a parameter
    @Query("DELETE FROM image_table WHERE image_link = :image")
    fun deleteImage(image: String)

}