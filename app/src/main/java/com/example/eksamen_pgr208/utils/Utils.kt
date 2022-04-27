package com.example.eksamen_pgr208.utils

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.eksamen_pgr208.R
import com.example.eksamen_pgr208.activities.FullscreenResultActivity
import com.example.eksamen_pgr208.activities.MainActivity
import com.example.eksamen_pgr208.adapter.model.ImageDatabaseModel
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.nio.file.Files

fun fileCompresser(TAG: String, file: File): File? {
    return try {

        // BitmapFactory options to downsize the image
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inDensity = 10
        // factor of downsizing the image
        var inputStream = FileInputStream(file)

        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream.close()

        // The new size we want to scale to
        val size = 250

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (options.outWidth / scale / 2 >= size &&
            options.outHeight / scale / 2 >= size
        ) {
            scale *= 2
        }
        val options2 = BitmapFactory.Options()
        options2.inSampleSize = scale
        inputStream = FileInputStream(file)
        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, options2)
        inputStream.close()

        // here we override the original image file
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        selectedBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        file
    } catch (e: Exception) {
        Log.e(TAG, "Exception catched in fileCompresser", e)
        return null
    }
}

fun verifyPermissions(fullscreenResultActivity: FullscreenResultActivity): Boolean {
    // ask for permissions in Android 12 I think
    val permissionExternalMemory =
        ActivityCompat.checkSelfPermission(fullscreenResultActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
        val storagePermissionsArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(fullscreenResultActivity, storagePermissionsArray, 1)
        return false
    }
    return true
}

@RequiresApi(Build.VERSION_CODES.O)
@Suppress("BlockingMethodInNonBlockingContext")
fun saveImageToExternalStorage(TAG: String, fullscreenResultActivity: FullscreenResultActivity) {

    // checking if the user has already permitted access to write external storage
    if (!verifyPermissions(fullscreenResultActivity)) {
        Toast.makeText(fullscreenResultActivity, "This app needs permission to store photos on your device.", Toast.LENGTH_SHORT).show()
        return
    } else {
        CoroutineScope(Dispatchers.IO).launch {

            // looper to handle queues in thread
            // elvis operator to check if Looper.myLooper() is null, if it's null it will execute Looper.prepare()
            Looper.myLooper() ?: Looper.prepare()

            val outputStream : OutputStream
            val dir = File(Environment.getExternalStorageDirectory(), "Download")
            if (!dir.exists()) {
                // makes folder if it does not exist
                dir.mkdir()
            }

            val drawable = fullscreenResultActivity.binding.fullscreenImage.drawable as BitmapDrawable
            val bitmap = drawable.bitmap

            val file = File(dir, System.currentTimeMillis().toString() + ".png")
            try {
                outputStream = FileOutputStream(file)
                // compress file that are stored in storage
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                Log.i(TAG, "Successfully saved ${file.name} to the ${dir.name} folder")
                outputStream.flush()
                outputStream.close()
            } catch (e: FileNotFoundException) {
                e.stackTraceToString()
                Log.e(TAG, "Catched FileNotFoundException while trying to save image in storage", e)
            } finally {
                // compress file that are soon going to be byteArray before it gets added to database
                val compressed = fileCompresser(TAG, file)
                val imageByteArray : ByteArray = Files.readAllBytes(compressed?.toPath())
                addToDatabase(TAG, fullscreenResultActivity, imageByteArray)
            }
        }
        Toast.makeText(fullscreenResultActivity, "Image saved in 'Download' folder", Toast.LENGTH_SHORT).show()
    }
}

fun addToDatabase(TAG: String, fullscreenResultActivity: FullscreenResultActivity, images: ByteArray) {
    val image = ImageDatabaseModel(0, images)
    try {
        fullscreenResultActivity.imageViewModel.addImage(image)
        Log.i(TAG, "Object reference: ${image.image} added to database as blob")
    } catch (e: Exception) {
        Log.e(TAG, "Exception catched in saving to database", e)
    }
}

fun showCameraAndGalleryDialog(mainActivity: MainActivity) {
    // shows dialog (modal) to prompt the user to either choose camera or gallery
    val camOrGallDialog = Dialog(mainActivity)

    camOrGallDialog.setContentView(R.layout.dialog_camera_or_gallery)
    camOrGallDialog.setTitle("Choose source: ")

    val btnGallery : ImageButton = camOrGallDialog.findViewById(R.id.btn_gallery)
    val btnCamera : ImageButton = camOrGallDialog.findViewById(R.id.btn_camera)

    // removes background modal
    camOrGallDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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