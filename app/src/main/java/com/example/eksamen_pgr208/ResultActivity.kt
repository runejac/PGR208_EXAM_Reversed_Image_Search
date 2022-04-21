package com.example.eksamen_pgr208

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import java.util.ArrayList

private const val TAG = "ResultActivity"

class ResultActivity : AppCompatActivity(), ResultsAdapter.RecyclerClick {
    private lateinit var rvImage : RecyclerView
    private lateinit var imageViewModel : ImageViewModel
    private var btnClearSearchAndGoesBack : Button? = null
    private lateinit var binding : ResultActivityBinding


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ResultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val images: ArrayList<ImageModelResultItem>? = intent.getParcelableArrayListExtra("images")

        rvImage = binding.rvResults


        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        // todo: er ikke ordentlig i grid med størrelse = størrelse på bildet enda
        val gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        rvImage.layoutManager = layoutManager
        layoutManager.gapStrategy = gapStrategy
        rvImage.adapter = ResultsAdapter(this, images = ArrayList<ImageModelResultItem>(images!!), this)


        btnClearSearchAndGoesBack?.setOnClickListener{
            goesHomeAndClearOldSearch(images)
        }

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

/*    override fun onBackPressed() {
        //super.onBackPressed()
        MainActivity().uploadProgressbar?.visibility = View.GONE
        MainActivity().imageFromCameraOrGallery?.visibility = View.GONE
        MainActivity().tvIntroStepOne?.visibility = View.VISIBLE
        MainActivity().tvIntroStepTwo?.visibility = View.GONE
        Toast.makeText(MainActivity(),"onBackPressed",Toast.LENGTH_SHORT).show()
    }*/
    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))

    }

     private fun addToDatabase(imagePos: Int) {
         try {
             val images: ArrayList<ImageModelResultItem>? = intent.getParcelableArrayListExtra("images")
             val thumbNailLink = images!![imagePos].thumbnail_link
             val imageLink = images[imagePos].image_link
             val image = Image(0, thumbNailLink, imageLink)
             imageViewModel.addImage(image)
             println(image)
             Toast.makeText(this, "Successfully added image to database!", Toast.LENGTH_LONG).show()
             Log.i(TAG, "${image.image_link} added to database")
         } catch (e: Exception) {
             Log.e(TAG, "Crash in saving to database", e)
         }
     }

    override fun onImageClick(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Save image")
            .setMessage("Do you want to save the image?")
            .setPositiveButton("Yes") { dialog, _ ->
                addToDatabase(position);
            }
            .setNegativeButton("No") { dialog, _ ->
                Toast.makeText(this, "Image not added to database", Toast.LENGTH_SHORT).show()
            }
            .show()
    }


    private fun goesHomeAndClearOldSearch(list: ArrayList<ImageModelResultItem>) {
        list.clear()
        startActivity(Intent(this, MainActivity::class.java))
    }

}