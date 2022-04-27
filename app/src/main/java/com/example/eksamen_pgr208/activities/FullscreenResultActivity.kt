package com.example.eksamen_pgr208.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.androidnetworking.AndroidNetworking
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eksamen_pgr208.adapter.model.ImageDatabaseModel
import com.example.eksamen_pgr208.data.database.ImageViewModel
import com.example.eksamen_pgr208.adapter.model.ImageResultItemModel
import com.example.eksamen_pgr208.databinding.FullscreenResultActivityBinding
import com.example.eksamen_pgr208.utils.fileCompresser
import com.example.eksamen_pgr208.utils.saveImageToExternalStorage
import com.example.eksamen_pgr208.utils.verifyPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.nio.file.Files


private const val TAG = "FullscreenActivity"

class FullscreenResultActivity : AppCompatActivity() {


    lateinit var binding : FullscreenResultActivityBinding
    lateinit var imageViewModel : ImageViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FullscreenResultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gets intent object that shows results
        val data : ImageResultItemModel? = intent.getParcelableExtra("imageclickedfromresult")
        imageViewModel = ImageViewModel(this.application)

        binding.fabSaveImage.bringToFront()

        // displays image on fullscreen from result
        Glide.with(this)
            .load(data?.image_link)
            .transform(RoundedCorners(30))
            .into(binding.fullscreenImage)


        binding.fabSaveImage.setOnClickListener {

            // asks for permission to store and access users phone
            verifyPermissions(this)

            data?.let {

                AlertDialog.Builder(this)
                    .setTitle("Save image")
                    .setMessage("Do you want to save the image?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        saveImageToExternalStorage(TAG, this)
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
}