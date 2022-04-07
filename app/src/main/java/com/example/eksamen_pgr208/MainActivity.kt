package com.example.eksamen_pgr208

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*
    TESTING THIS FEATURE https://developer.android.com/about/versions/13/features/photopicker#kotlin

    val imageFromGallery = findViewById<ImageView>(R.id.imageview)
    imageFromGallery.setOnClickListener(openGallery())

     public void openGallery() {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        intent.type = "images/*" */
        startActivityForResult(intent, PHOTO_PICKER_REQUEST_CODE)
     } */


}