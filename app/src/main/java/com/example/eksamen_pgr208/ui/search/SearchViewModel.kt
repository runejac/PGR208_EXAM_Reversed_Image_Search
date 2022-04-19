package com.example.eksamen_pgr208.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {

    private val textValue = MutableLiveData<String>().apply {
        value = "Here will the image you pick appear and you will get a choice to search this image or go back"
    }

    val text : LiveData<String> = textValue

}
