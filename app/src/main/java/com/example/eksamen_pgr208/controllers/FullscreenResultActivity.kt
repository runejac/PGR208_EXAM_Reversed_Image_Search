package com.example.eksamen_pgr208.controllers

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import com.androidnetworking.AndroidNetworking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.nio.file.Files
import android.graphics.BitmapFactory





private const val TAG = "FullscreenActivity"

class FullscreenResultActivity : AppCompatActivity() {


    private lateinit var binding : FullscreenActivityBinding
    private lateinit var imageViewModel : ImageViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FullscreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gets intent object that shows results
        val data : ImageModelResultItem? = intent.getParcelableExtra("imageclickedfromresult")
        imageViewModel = ImageViewModel(this.application)

        binding.fabSaveImage.bringToFront()

        // displays image on fullscreen from result
        Glide.with(this)
            .load(data?.image_link)
            .transform(RoundedCorners(30))
            .into(binding.fullscreenImage)


        binding.fabSaveImage.setOnClickListener {

            // asks for permission to store and access users phone
            verifyPermissions()

            data?.let {

                AlertDialog.Builder(this)
                    .setTitle("Save image")
                    .setMessage("Do you want to save the image?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        saveImage()
                        Toast.makeText(this, "Successfully added image to database!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show()
                    }
                    .show()
                // Elvis operator
            } ?: Log.e(TAG, "Error occured while saving image")
        }


        binding.ibCancel.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AndroidNetworking.forceCancelAll()
    }


    private fun addToDatabase(images: ByteArray) {
        val image = Image(0, images)
        try {
            imageViewModel.addImage(image)
            Log.i(TAG, "Object reference: ${image.image} added to database as blob")
        } catch (e: Exception) {
            Log.e(TAG, "Exception catched in saving to database", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("BlockingMethodInNonBlockingContext")
    private fun saveImage() {

        // checking if the user has already permitted access to write external storage
        if (!verifyPermissions()) {
            Toast.makeText(this, "This app needs permission to store photos on your device.", Toast.LENGTH_SHORT).show()
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

                val drawable = binding.fullscreenImage.drawable as BitmapDrawable
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
                    val compressed = fileCompresser(file)
                    val imageByteArray : ByteArray = Files.readAllBytes(compressed?.toPath())
                    addToDatabase(imageByteArray)
                }
                Toast.makeText(this@FullscreenResultActivity, "Image saved in 'Download' folder", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun fileCompresser(file: File): File? {
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