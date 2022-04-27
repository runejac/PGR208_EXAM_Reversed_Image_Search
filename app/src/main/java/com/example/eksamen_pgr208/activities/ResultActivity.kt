package com.example.eksamen_pgr208.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eksamen_pgr208.R
import com.example.eksamen_pgr208.adapter.ResultsAdapter
import com.example.eksamen_pgr208.data.database.ImageViewModel
import com.example.eksamen_pgr208.adapter.model.ImageResultItemModel
import com.example.eksamen_pgr208.databinding.ResultActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

private const val TAG = "ResultActivity"

class ResultActivity : AppCompatActivity(), ResultsAdapter.RecyclerClick {
    private lateinit var imageViewModel : ImageViewModel
    private lateinit var binding : ResultActivityBinding
    private var imageModels : ArrayList<ImageResultItemModel> = ArrayList()
    private var imageSearchedOn : String = ""


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // using intent from Mainactivity to show imageModels searched on
        try {
            imageSearchedOn = intent.getStringExtra("image_searched_on")!!
        } catch (e: NullPointerException) {
            Log.e(TAG, "Catched a NullPointerException. $imageSearchedOn is null", e)
        }



        // same as above, just for the results
        try {
            imageModels = intent.getParcelableArrayListExtra("imageresults")!!
        } catch (e: NullPointerException) {
            Log.e(TAG, "Catched a NullPointerException. $imageModels is null", e)
        }

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        // setting the layout
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvResults.layoutManager = layoutManager

        // send image results objects to adapter, to be used in recyclerview
        binding.rvResults.adapter = ResultsAdapter(this, imageModels = ArrayList<ImageResultItemModel>(imageModels), this)


        // showing image searched on
        Glide.with(this)
            .load(imageSearchedOn)
            .fitCenter()
            .transform(RoundedCorners(15))
            .into(binding.ivSearched)


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
                    startActivity(Intent(this, SavedActivity::class.java))
                    overridePendingTransition(0, 0)
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


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }


    override fun onImageClick(position: Int) {

        // intents image clicked on to pass to FullscreenActivity for further functionality
        imageModels.let {
            val imageClickedFromResult = Intent(this, FullscreenResultActivity::class.java)
            imageClickedFromResult.putExtra("imageclickedfromresult", imageModels[position])
            startActivity(imageClickedFromResult)
        }

    }
}