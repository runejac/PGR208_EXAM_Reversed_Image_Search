package com.example.eksamen_pgr208.controllers

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eksamen_pgr208.data.Image
import com.example.eksamen_pgr208.data.ImageViewModel
import com.example.eksamen_pgr208.databinding.FullscreenSavedActivityBinding

class FullscreenSavedActivity : AppCompatActivity() {

    private lateinit var binding : FullscreenSavedActivityBinding
    private lateinit var imageViewModel : ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FullscreenSavedActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gets intent object from SavedActivity, from the ArrayList that mirrors the database
        val data : Image? = intent.getParcelableExtra("imageclickedfromsaved")
        imageViewModel = ImageViewModel(this.application)


        data?.let {

            // displays image on fullscreen from database
            Glide.with(this)
                .load(data.image)
                .transform(RoundedCorners(30))
                .into(binding.fullscreenImageSaved)


            binding.fabDeleteImage.setOnClickListener {

                AlertDialog.Builder(this)
                    .setTitle("Delete image")
                    .setMessage("Do you want to delete the image?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        imageViewModel.deleteImage(data)
                        Toast.makeText(this, "Image deleted from database", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        Toast.makeText(this, "Image not deleted", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
        }

        binding.ibCancel.setOnClickListener {
            finish()
        }
    }

}