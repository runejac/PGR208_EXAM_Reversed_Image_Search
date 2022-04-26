package com.example.eksamen_pgr208.data

import android.app.Application
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Image>>
    private val repo: ImageRepo

    init {
        val imageDao = ImageDatabase.getDataBase(application).imageDao()
        repo = ImageRepo(imageDao)
        readAllData = repo.readAllData
    }

    fun addImage(image: Image) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addImage(image)
        }
    }

    fun deleteImage(image: Image) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteImage(image)
        }
    }


}