package com.example.eksamen_pgr208

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker

class MainActivity : AppCompatActivity() {

    private lateinit var imageFromCameraOrMemory : ImageView

    //TODO: Sette opp FAN (https://github.com/amitshekhariitbhu/Fast-Android-Networking)
    //      Teste endpoints i Postman
    //      Sette opp SQLite (bruke Room: https://developer.android.com/training/data-storage/room)
    //      Håndtere alle lifecycle-states


    // Good to be join ya -Stian Ihlr
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageFromCameraOrMemory = findViewById(R.id.addedImageFromEitherCameraOrMemory)
        val btnGalleryOrCameraDialog : ImageButton = findViewById(R.id.btn_gallery_or_camera)

        btnGalleryOrCameraDialog.setOnClickListener {
            println("Hello from gallery or camera button")
            showCameraAndGalleryDialog()
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri : Uri = data?.data!!
                println(uri.toString())
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        /*if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            imageFromCameraOrMemory.setImageURI(data?.data)
        }*/
    }


    // shows dialog (modal) to prompt the user to either choose camera or gallery
    private fun showCameraAndGalleryDialog() {
        val camOrGallDialog = Dialog(this)
        camOrGallDialog.setContentView(R.layout.dialog_camera_or_gallery)
        camOrGallDialog.setTitle("Choose source: ")
        val btnGallery : ImageButton = camOrGallDialog.findViewById(R.id.btn_gallery)
        val btnCamera : ImageButton = camOrGallDialog.findViewById(R.id.btn_camera)


        btnGallery.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(arrayOf("image/*"))
                .maxResultSize(400, 400)
                .crop()
                .start()

            camOrGallDialog.dismiss()

            println("gallery clicked")
        }

        btnCamera.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .maxResultSize(400, 400)
                .crop()
                .start()

            camOrGallDialog.dismiss()


            println("camera clicked")
        }

        camOrGallDialog.show()
    }


}