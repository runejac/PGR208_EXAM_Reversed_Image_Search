package com.example.eksamen_pgr208.data

import androidx.lifecycle.LiveData


// Alle filer under data-folderen er skrevet ved hjelp av denne videoen:
// https://www.youtube.com/watch?v=lwAvI3WDXBY
// anbefaler å se den

class ImageRepo(private val imageDao: ImageDao) {

    val readAllData: LiveData<List<Image>> = imageDao.readAllData()

    suspend fun addImage(image: Image) {
        imageDao.addImage(image)
    }

}