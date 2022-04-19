package com.example.eksamen_pgr208.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SavedViewModel : ViewModel(){
    private val textValue = MutableLiveData<String>().apply {
        value = "This is Saved fragment"
    }

    val text : LiveData<String> = textValue
}
