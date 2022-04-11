package com.example.eksamen_pgr208.ui.home

import android.media.Image
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){

    private val textValue = MutableLiveData<String>().apply {
        value = "REVERSE IMAGE SEARCH"

    }

    private val subTextValue = MutableLiveData<String>().apply {
        value = "Search for any image you want and get great results back"
    }

    val subText : LiveData<String> = subTextValue
    val text: LiveData<String> = textValue

}
