package com.example.eksamen_pgr208

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eksamen_pgr208.data.Image
import com.example.eksamen_pgr208.data.ImageViewModel
import com.example.eksamen_pgr208.data.api.ImageModelResultItem
import com.example.eksamen_pgr208.databinding.FullscreenActivityBinding
import android.os.Environment
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import java.io.*


private const val TAG = "FullscreenActivity"

class FullscreenActivity:AppCompatActivity() {


    private lateinit var binding : FullscreenActivityBinding
    private lateinit var imageViewModel : ImageViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FullscreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data : ImageModelResultItem? = intent.getParcelableExtra("imageclicked")
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        binding.fabSaveImage.bringToFront()


        Glide.with(this)
            .load(data?.image_link)
            .transform(RoundedCorners(30))
            .into(binding.fullscreenImage)


        binding.fabSaveImage.setOnClickListener {

            data?.let {

                AlertDialog.Builder(this)
                    .setTitle("Save image")
                    .setMessage("Do you want to save the image?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        addToDatabase(data)
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            } ?: Log.e(TAG, "Error occured while saving image")
        }


        binding.ibCancel.setOnClickListener {
            finish()
        }
    }



    private fun addToDatabase(images: ImageModelResultItem) {
        try {
            val thumbNailLink = images.thumbnail_link
            val imageLink = images.image_link
            val image = Image(0, thumbNailLink, imageLink)
            imageViewModel.addImage(image)
            println(image)
            saveImage()
            Toast.makeText(this, "Successfully added image to database!", Toast.LENGTH_LONG).show()
            Log.i(TAG, "${image.image_link} added to database")
        } catch (e: Exception) {
            Log.e(TAG, "Crash in saving to database", e)
        }
    }


    private fun saveImage() {

        // checking if the user has already permitted access to write external storage
        if (!verifyPermissions()) {
            return
        } else {
            var outputStream : OutputStream? = null
            val dir = File(Environment.getExternalStorageDirectory(), "Download")
            if (!dir.exists()) {
                // makes folder if it does not exist
                dir.mkdir()
            }

            val drawable = binding.fullscreenImage.drawable as BitmapDrawable
            val bitmap = drawable.bitmap

            val file = File(dir, System.currentTimeMillis().toString() + ".png")
            try {
                outputStream = FileOutputStream(file)
                Log.i(TAG, "Successfully saved ${file.name} to the ${dir.name} folder")
            } catch (e: FileNotFoundException) {
                e.stackTraceToString()
                Log.e(TAG, "Catched FileNotFoundException while trying to save image in storage", e)
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            Toast.makeText(this, "Image saved in 'Download' folder", Toast.LENGTH_SHORT).show()
            try {
                outputStream?.flush()
            } catch (e: IOException) {
                e.stackTraceToString()
                Log.e(TAG, "Catched IOException in trying to flush the content to outputStream while saving image in storage", e)
            }
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.stackTraceToString()
                Log.e(TAG, "Catched IOException in trying to close outputStream while saving image in storage", e)
            }
        }
    }


    private fun verifyPermissions(): Boolean {
        // ask for permissions in Android 12 I think
        val permissionExternalMemory =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            val storagePermissionsArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, storagePermissionsArray, 1)
            return false
        }
        return true
    }
}