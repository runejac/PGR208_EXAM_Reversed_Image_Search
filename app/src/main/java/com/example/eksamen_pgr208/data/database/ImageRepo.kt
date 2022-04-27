package com.example.eksamen_pgr208.data.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.eksamen_pgr208.adapter.model.ImageDatabaseModel

class ImageRepo(private val imageDao: ImageDao) {

    val readAllData: LiveData<List<ImageDatabaseModel>> = imageDao.readAllData()

    suspend fun addImage(imageDatabaseModel: ImageDatabaseModel) {
        val id = imageDao.addImage(imageDatabaseModel)
        Log.i("ImageRepo", "ByteArray image added to database: $imageDatabaseModel")
        Log.i("ImageRepo", "ID: $id")
    }


    fun deleteImage(imageDatabaseModel: ImageDatabaseModel) {
        imageDao.deleteImage(imageDatabaseModel)
        Log.i("ImageRepo", "Object reference ${imageDatabaseModel.image.toString()} deleted from database")
    }

}