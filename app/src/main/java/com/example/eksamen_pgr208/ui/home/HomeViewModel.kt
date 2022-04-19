package com.example.eksamen_pgr208.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){

    private val textValue = MutableLiveData<String>().apply {
        value = "REVERSED IMAGE SEARCH"
    }

    private val subTextValue = MutableLiveData<String>().apply {
        value = "Search for any image you want and get great results back"
    }

    val subText : LiveData<String> = subTextValue
    val text: LiveData<String> = textValue

}
