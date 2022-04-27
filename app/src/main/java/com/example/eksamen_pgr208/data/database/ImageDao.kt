package com.example.eksamen_pgr208.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.eksamen_pgr208.adapter.model.ImageDatabaseModel

@Dao
interface ImageDao {

    // OnConflictStrategy.REPLACE means that if the user adds
    // the same imageDatabaseModel multiple times, it will be replaced each time
    // so that the table in the database doesn't fill up with copies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage(imageDatabaseModel: ImageDatabaseModel): Long

    @Query("SELECT * FROM image_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<ImageDatabaseModel>>

    @Delete
    fun deleteImage(imageDatabaseModel: ImageDatabaseModel)

}