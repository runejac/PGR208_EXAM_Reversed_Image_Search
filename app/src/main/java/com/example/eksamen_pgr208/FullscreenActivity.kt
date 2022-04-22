package com.example.eksamen_pgr208

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
import com.example.eksamen_pgr208.databinding.ActivityMainBinding
import com.example.eksamen_pgr208.databinding.FullscreenActivityBinding

private const val TAG = "FullscreenActivity"

class FullscreenActivity:AppCompatActivity() {


    private lateinit var binding : FullscreenActivityBinding
    private lateinit var imageViewModel : ImageViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FullscreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Log.i("FullscreenActivity", "fullscreen clicked")

        val data : ImageModelResultItem? = intent.getParcelableExtra("imageclicked")
        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]




        Glide.with(this)
            .load(data?.image_link)
            .transform(RoundedCorners(30))
            .into(binding.fullscreenImage)

        binding.saveImage.setOnClickListener {
            data?.let {

                AlertDialog.Builder(this)
                    .setTitle("Save image")
                    .setMessage("Do you want to save the image?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        addToDatabase(data)
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        Toast.makeText(this, "Image not saved", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            } ?: Log.e(TAG, "Error occured while saving image")
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun addToDatabase(images: ImageModelResultItem) {
        try {
            //val images: ArrayList<ImageModelResultItem>? = intent.getParcelableArrayListExtra("images")
            val thumbNailLink = images.thumbnail_link
            val imageLink = images.image_link
            val image = Image(0, thumbNailLink, imageLink)
            imageViewModel.addImage(image)
            println(image)
            Toast.makeText(this, "Successfully added image to database!", Toast.LENGTH_LONG).show()
            Log.i(TAG, "${image.image_link} added to database")
        } catch (e: Exception) {
            Log.e(TAG, "Crash in saving to database", e)
        }
    }


}