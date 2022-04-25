package com.example.eksamen_pgr208

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eksamen_pgr208.adapters.ResultsAdapter
import com.example.eksamen_pgr208.adapters.SavedAdapter
import com.example.eksamen_pgr208.data.Image
import com.example.eksamen_pgr208.data.ImageViewModel
import com.example.eksamen_pgr208.databinding.SavedActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "SavedActivity"

class SavedActivity : AppCompatActivity(), ResultsAdapter.RecyclerClick {

    private lateinit var imageViewModel : ImageViewModel
    private lateinit var binding : SavedActivityBinding
    private var imagesFromDbList : ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SavedActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        // setting the layout
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvSaved.layoutManager = layoutManager


        // using data with livedata from database, to be used in adapter recyclerview
        imageViewModel.readAllData.observe(this) { image ->

            // adding image_link from db to new list
            imagesFromDbToViews(image)

            imagesFromDbList?.let {
                binding.rvSaved.adapter = SavedAdapter(this, images = ArrayList(imagesFromDbList!!), this)
            } ?: Log.e(TAG, "Error in trying to send arguments from $TAG to SavedAdapter")

        }


        // Get bottom navigation shadow be gone
        val nav : BottomNavigationView = binding.bottomNavigationView
        nav.selectedItemId = R.id.saved
        nav.background = null

        // navbar
        // Lambda function used
        nav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.saved -> {
                    true
                }

                else -> false
            }
        }

    }


    private fun deleteFromDatabase(imagePos: Int) {
        try {
            imageViewModel.deleteImage(imagesFromDbList!![imagePos])
            Toast.makeText(this, "Image deleted!", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Image deleted")
        } catch (e: Exception) {
            Log.e(TAG, "Crash in deleting image", e)
        }
    }

    // targeting the image to be deleted
    override fun onImageClick(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete image")
            .setMessage("Do you want to delete the image?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteFromDatabase(position);
            }
            .setNegativeButton("No") { dialog, _ ->
                Toast.makeText(this, "Image not deleted", Toast.LENGTH_SHORT).show()
            }
            .show()
    }


    // from database to list
    private fun imagesFromDbToViews(image: List<Image>) {
        imagesFromDbList = image.map(Image::image_link) as ArrayList<String>
        Log.i(TAG, "Images from database to list to be used in view: $imagesFromDbList")
    }
}