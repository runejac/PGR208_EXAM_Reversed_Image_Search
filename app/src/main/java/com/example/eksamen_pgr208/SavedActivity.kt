package com.example.eksamen_pgr208

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eksamen_pgr208.adapters.ResultsAdapter
import com.example.eksamen_pgr208.adapters.SavedAdapter
import com.example.eksamen_pgr208.data.Image
import com.example.eksamen_pgr208.data.ImageViewModel
import kotlinx.android.synthetic.main.image_rv_layout.*
import kotlinx.android.synthetic.main.saved_activity.*

private const val TAG = "SavedActivity"

class SavedActivity : AppCompatActivity(), ResultsAdapter.RecyclerClick {

    private lateinit var rvSavedImage : RecyclerView
    private lateinit var imageViewModel : ImageViewModel
    private var imageResult : ImageButton? = null
    private var imagesFromDbList : ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_activity)

        rvSavedImage = rv_saved
        imageResult = image_result

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        rvSavedImage.layoutManager = layoutManager
        layoutManager.gapStrategy = gapStrategy

        imageViewModel.readAllData.observe(this) { image ->
            imagesFromDbToViews(image)
            rvSavedImage.adapter = SavedAdapter(this, images = ArrayList(imagesFromDbList!!), this)
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

    override fun onImageClick(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Hi!")
            .setMessage("Delete image?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteFromDatabase(position);
            }
            .setNegativeButton("No") { dialog, _ ->
                Toast.makeText(this, "Image not deleted", Toast.LENGTH_SHORT).show()
            }
            .show()
    }


    private fun imagesFromDbToViews(image: List<Image>) {
        imagesFromDbList = image.map(Image::image_link) as ArrayList<String>
        println(imagesFromDbList)
    }

}