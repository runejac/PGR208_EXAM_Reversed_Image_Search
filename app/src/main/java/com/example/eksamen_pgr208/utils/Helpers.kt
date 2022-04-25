package com.example.eksamen_pgr208.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eksamen_pgr208.MainActivity
import com.example.eksamen_pgr208.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil


class Helpers  {
    companion object {



        // TODO se om vi kan få refaktorert noe metoder som har med bilde, valg av bilde, save osv her etter hvert
        fun showCameraAndGalleryDialog(mainActivity: MainActivity) {
            // shows dialog (modal) to prompt the user to either choose camera or gallery
            val camOrGallDialog = Dialog(mainActivity)

            camOrGallDialog.setContentView(R.layout.dialog_camera_or_gallery)
            camOrGallDialog.setTitle("Choose source: ")

            val btnGallery : ImageButton = camOrGallDialog.findViewById(R.id.btn_gallery)
            val btnCamera : ImageButton = camOrGallDialog.findViewById(R.id.btn_camera)

            btnGallery.setOnClickListener {
                ImagePicker.with(mainActivity)
                    .galleryOnly()
                    .galleryMimeTypes(arrayOf("image/*"))
                    .maxResultSize(600, 600)
                    .compress(1024)
                    .start()
                camOrGallDialog.dismiss()

                println("gallery clicked")
            }

            btnCamera.setOnClickListener {
                ImagePicker.with(mainActivity)
                    .cameraOnly()
                    .maxResultSize(600, 600)
                    .compress(1024)
                    .start()
                camOrGallDialog.dismiss()
                println("camera clicked")
            }
            camOrGallDialog.show()
        }

    }
}