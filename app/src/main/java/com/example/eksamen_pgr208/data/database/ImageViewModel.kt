package com.example.eksamen_pgr208.data.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.eksamen_pgr208.adapter.model.ImageDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<ImageDatabaseModel>>
    private val repo: ImageRepo

    init {
        val imageDao = ImageDatabase.getDataBase(application).imageDao()
        repo = ImageRepo(imageDao)
        readAllData = repo.readAllData
    }

    fun addImage(imageDatabaseModel: ImageDatabaseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addImage(imageDatabaseModel)
        }
    }

    fun deleteImage(imageDatabaseModel: ImageDatabaseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteImage(imageDatabaseModel)
        }
    }


}