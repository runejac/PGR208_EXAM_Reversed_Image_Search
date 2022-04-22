package com.example.eksamen_pgr208

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.example.eksamen_pgr208.adapters.ResultsAdapter
import com.example.eksamen_pgr208.data.Image
import com.example.eksamen_pgr208.data.ImageViewModel
import com.example.eksamen_pgr208.data.api.ImageModelResultItem
import com.example.eksamen_pgr208.databinding.ResultActivityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.image_rv_layout.*
import kotlinx.android.synthetic.main.result_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.util.ArrayList

private const val TAG = "ResultActivity"

class ResultActivity : AppCompatActivity(), ResultsAdapter.RecyclerClick {
    private lateinit var rvImage : RecyclerView
    private lateinit var imageViewModel : ImageViewModel
    private lateinit var binding : ResultActivityBinding
    private var images : ArrayList<ImageModelResultItem> = ArrayList()



    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvImage = binding.rvResults

        try {
            images = intent.getParcelableArrayListExtra("images")!!
        } catch (e: NullPointerException) {
            Log.e(TAG, "Catched a NullPointerException. $images is null", e)
        }

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvImage.layoutManager = layoutManager

        // send image objects to adapter
        rvImage.adapter = ResultsAdapter(this, images = ArrayList<ImageModelResultItem>(images), this)


        // Get bottom navigation shadow be gone
        val nav : BottomNavigationView = binding.bottomNavigationView
        nav.selectedItemId = R.id.saved
        nav.background = null


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


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }


    override fun onImageClick(position: Int) {

        images.let {
            val imageClicked = Intent(this, FullscreenActivity::class.java)
            imageClicked.putExtra("imageclicked", images[position])
            startActivity(imageClicked)
        }

    }
}