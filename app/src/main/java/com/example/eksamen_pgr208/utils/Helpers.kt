package com.example.eksamen_pgr208.utils

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.widget.ImageButton
import com.example.eksamen_pgr208.MainActivity
import com.example.eksamen_pgr208.R
import com.github.dhaval2404.imagepicker.ImagePicker


class Helpers  {
    companion object {



        // TODO se om vi kan få refaktorert noe metoder som har med bilde, valg av bilde, save osv her etter hvert
        // Make callbacks
        fun showCameraAndGalleryDialog(mainActivity: MainActivity) {
            // shows dialog (modal) to prompt the user to either choose camera or gallery
            val camOrGallDialog = Dialog(mainActivity)

            camOrGallDialog.setContentView(R.layout.dialog_camera_or_gallery)
            camOrGallDialog.setTitle("Choose source: ")

            val btnGallery : ImageButton = camOrGallDialog.findViewById(R.id.btn_gallery)
            val btnCamera : ImageButton = camOrGallDialog.findViewById(R.id.btn_camera)

            // removes background modal
            camOrGallDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

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