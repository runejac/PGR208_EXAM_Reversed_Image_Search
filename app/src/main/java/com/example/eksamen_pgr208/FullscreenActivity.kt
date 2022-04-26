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
import android.os.Build
import android.os.Looper
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.androidnetworking.AndroidNetworking
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.nio.file.Files
import android.graphics.BitmapFactory





private const val TAG = "FullscreenActivity"

class FullscreenActivity:AppCompatActivity() {


    private lateinit var binding : FullscreenActivityBinding
    private lateinit var imageViewModel : ImageViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FullscreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data : ImageModelResultItem? = intent.getParcelableExtra("imageclicked")
        imageViewModel = ImageViewModel(this.application) /*ViewModelProvider(this)[ImageViewModel::class.java]*/

        binding.fabSaveImage.bringToFront()


        Glide.with(this)
            .load(data?.image_link)
            .transform(RoundedCorners(30))
            .into(binding.fullscreenImage)


        binding.fabSaveImage.setOnClickListener {

            verifyPermissions()

            data?.let {

                AlertDialog.Builder(this)
                    .setTitle("Save image")
                    .setMessage("Do you want to save the image?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        saveImage()
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

        Log.i(TAG, "images before: $images")
        val image = Image(0, images)
        Log.i(TAG, "images after: $image")
        try {
            imageViewModel.addImage1(image)
            Toast.makeText(this, "Successfully added image to database!", Toast.LENGTH_LONG).show()
            Log.i(TAG, "${image.image} added to database")
        } catch (e: Exception) {
            Log.e(TAG, "Crash in saving to database", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("BlockingMethodInNonBlockingContext")
    private fun saveImage() {

        var file : File? = null

        // checking if the user has already permitted access to write external storage
        if (!verifyPermissions()) {
            Toast.makeText(this, "This app needs permission to store photos on your device.", Toast.LENGTH_SHORT).show()
            return
        } else {
            CoroutineScope(Dispatchers.IO).launch {

                // looper to handle queues in thread
                // elvis operator to check if Looper.myLooper() is null, if it's null it will execute Looper.prepare()
                Looper.myLooper() ?: Looper.prepare()

                var outputStream : OutputStream? = null
                val dir = File(Environment.getExternalStorageDirectory(), "Download")
                if (!dir.exists()) {
                    // makes folder if it does not exist
                    dir.mkdir()
                }

                val drawable = binding.fullscreenImage.drawable as BitmapDrawable
                val bitmap = drawable.bitmap

                file = File(dir, System.currentTimeMillis().toString() + ".png")
                try {
                    outputStream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream)
                    Log.i(TAG, "Successfully saved ${file!!.name} to the ${dir.name} folder")
                    outputStream.flush()
                    outputStream.close()
                } catch (e: FileNotFoundException) {
                    e.stackTraceToString()
                    Log.e(TAG, "Catched FileNotFoundException while trying to save image in storage", e)
                } finally {

                    val compressed = saveBitmapToFile(file!!)

                    val imageByteArray : ByteArray = Files.readAllBytes(compressed?.toPath())

                    addToDatabase(imageByteArray)
                }
                Toast.makeText(this@FullscreenActivity, "Image saved in 'Download' folder", Toast.LENGTH_SHORT).show()
            }

        }
    }

    // from stackoverflow https://stackoverflow.com/questions/18573774/how-to-reduce-an-image-file-size-before-uploading-to-a-server
    private fun saveBitmapToFile(file: File): File? {
        return try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            file
        } catch (e: java.lang.Exception) {
            null
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