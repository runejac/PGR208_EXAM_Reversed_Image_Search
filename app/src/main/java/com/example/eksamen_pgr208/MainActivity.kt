package com.example.eksamen_pgr208

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker

class MainActivity : AppCompatActivity() {

    private lateinit var imageFromGallery:ImageView

    //TODO: Sette opp FAN (https://github.com/amitshekhariitbhu/Fast-Android-Networking)
    //      Teste endpoints i Postman
    //      Sette opp SQLite (bruke Room: https://developer.android.com/training/data-storage/room)
    //      Håndtere alle lifecycle-states

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageFromGallery = findViewById(R.id.addedImage)
        val galleryBtn = findViewById<ImageButton>(R.id.galleryBtn)
        val cameraBtn = findViewById<ImageButton>(R.id.cameraBtn)

        galleryBtn.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(arrayOf("image/*"))
                .maxResultSize(400, 400)
                .crop()
                .start()
        }

        cameraBtn.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .maxResultSize(400, 400)
                .crop()
                .start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            imageFromGallery.setImageURI(data?.data)
        }
    }


}