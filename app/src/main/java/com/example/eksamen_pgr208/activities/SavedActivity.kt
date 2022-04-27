package com.example.eksamen_pgr208.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.example.eksamen_pgr208.R
import com.example.eksamen_pgr208.adapter.ResultsAdapter
import com.example.eksamen_pgr208.adapter.SavedAdapter
import com.example.eksamen_pgr208.adapter.model.ImageDatabaseModel
import com.example.eksamen_pgr208.data.database.ImageViewModel
import com.example.eksamen_pgr208.databinding.SavedActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "SavedActivity"

class SavedActivity : AppCompatActivity(), ResultsAdapter.RecyclerClick {
    private lateinit var imageViewModel : ImageViewModel
    private lateinit var binding : SavedActivityBinding
    private var imagesFromDbListMirror : ArrayList<ImageDatabaseModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SavedActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        // using data with livedata from database, to be used in adapter recyclerview
        imageViewModel.readAllData.observe(this) { image ->

            // mirrors the database list
            imagesFromDbListMirror = image as ArrayList<ImageDatabaseModel>

            // adding image_link from db to new list
            binding.rvSaved.adapter = SavedAdapter(this, image, this)
        }

        println(imagesFromDbListMirror.size)


        // setting the layout
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvSaved.layoutManager = layoutManager


        // Get bottom navigation shadow be gone
        val nav : BottomNavigationView = binding.bottomNavigationView
        nav.selectedItemId = R.id.saved
        nav.background = null

        // navbar
        // Lambda function
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


    // targeting the image to be deleted through arraylist that mirrors database objects
    override fun onImageClick(position: Int) {
        imagesFromDbListMirror.let {
                val imageClickedFromSaved = Intent(this, FullscreenSavedActivity::class.java)
                imageClickedFromSaved.putExtra("imageclickedfromsaved", imagesFromDbListMirror[position])
                startActivity(imageClickedFromSaved)
        }
    }
}