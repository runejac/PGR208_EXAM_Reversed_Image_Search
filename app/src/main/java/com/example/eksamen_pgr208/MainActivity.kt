package com.example.eksamen_pgr208

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker

class MainActivity : AppCompatActivity() {

    lateinit var imageFromGallery:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageFromGallery = findViewById(R.id.addedImage)
        val galleryBtn = findViewById<Button>(R.id.galleryBtn)
        val cameraBtn = findViewById<Button>(R.id.cameraBtn)

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