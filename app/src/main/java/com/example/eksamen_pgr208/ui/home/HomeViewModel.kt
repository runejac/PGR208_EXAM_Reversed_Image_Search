package com.example.eksamen_pgr208.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){

    private val textValue = MutableLiveData<String>().apply {
        value = "This is image reversed search"
    }
    val text: LiveData<String> = textValue

}
