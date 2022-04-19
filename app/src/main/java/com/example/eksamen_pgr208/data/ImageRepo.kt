package com.example.eksamen_pgr208.data

import androidx.lifecycle.LiveData

class ImageRepo(private val imageDao: ImageDao) {

    val readAllData: LiveData<List<Image>> = imageDao.readAllData()

    suspend fun addImage(image: Image) {
        imageDao.addImage(image)
    }

    fun deleteImage(image: String) {
        imageDao.deleteImage(image)
    }

}