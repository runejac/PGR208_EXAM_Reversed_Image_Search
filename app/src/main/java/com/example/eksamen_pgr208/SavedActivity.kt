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
import com.androidnetworking.AndroidNetworking
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
    private var imagesFromDbList : ArrayList<Image>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SavedActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]




        // using data with livedata from database, to be used in adapter recyclerview
        imageViewModel.readAllData.observe(this) { image ->

            imagesFromDbList = image as ArrayList<Image>

            // adding image_link from db to new list
            binding.rvSaved.adapter = SavedAdapter(this, image, this)
        }

        // setting the layout
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvSaved.layoutManager = layoutManager


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

    override fun onDestroy() {
        super.onDestroy()
        AndroidNetworking.forceCancelAll()
    }


    private fun deleteFromDatabase(imagePos: Int) {

        try {
            imagesFromDbList?.get(imagePos)?.let { itemPos ->
                imageViewModel.deleteImage(itemPos)
                Toast.makeText(this, "Image deleted!", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "${imagesFromDbList!![imagePos]} deleted from database")
            }
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
                deleteFromDatabase(position)
            }
            .setNegativeButton("No") { dialog, _ ->
                Toast.makeText(this, "Image not deleted", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}