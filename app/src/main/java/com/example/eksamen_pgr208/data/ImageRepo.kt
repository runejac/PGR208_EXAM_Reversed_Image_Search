package com.example.eksamen_pgr208.data

import androidx.lifecycle.LiveData

class ImageRepo(private val imageDao: ImageDao) {

    val readAllData: LiveData<List<Image>> = imageDao.readAllData()

    suspend fun addImage2(image: Image) {
        val a = imageDao.addImage3(image)
        println("image from addImage2 fra ImageRepo.kt: " + image)
        println("imageDao.addImage(image): " + a)
    }

    fun deleteImage(image: Image) {
        imageDao.deleteImage(image)
    }

}