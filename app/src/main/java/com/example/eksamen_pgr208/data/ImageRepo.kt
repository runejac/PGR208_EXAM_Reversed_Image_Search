package com.example.eksamen_pgr208.data

import android.util.Log
import androidx.lifecycle.LiveData

class ImageRepo(private val imageDao: ImageDao) {

    val readAllData: LiveData<List<Image>> = imageDao.readAllData()

    suspend fun addImage(image: Image) {
        val id = imageDao.addImage(image)
        Log.i("ImageRepo", "ByteArray image added to database: $image")
        Log.i("ImageRepo", "ID: $id")
    }


    fun deleteImage(image: Image) {
        imageDao.deleteImage(image)
        Log.i("ImageRepo", "Object reference ${image.image.toString()} deleted from database")
    }

}