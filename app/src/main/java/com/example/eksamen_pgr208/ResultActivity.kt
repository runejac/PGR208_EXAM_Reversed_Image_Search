package com.example.eksamen_pgr208

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eksamen_pgr208.adapters.ResultsAdapter
import com.example.eksamen_pgr208.data.Image
import com.example.eksamen_pgr208.data.ImageViewModel
import com.example.eksamen_pgr208.data.api.ImageModelResultItem
import kotlinx.android.synthetic.main.image_rv_layout.*
import kotlinx.android.synthetic.main.result_activity.*
import java.util.ArrayList

private const val TAG = "ResultActivity"

class ResultActivity : AppCompatActivity(), ResultsAdapter.RecyclerClick {
    private lateinit var rvImage : RecyclerView
    private var btnDeleteList : Button? = null
    private var imageResult : ImageButton? = null

    private lateinit var imageViewModel: ImageViewModel

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        val images: ArrayList<ImageModelResultItem>? = intent.getParcelableArrayListExtra("images")

        rvImage = rv_results
        btnDeleteList = btn_delete_list
        imageResult = image_result



        // livedata ikke i bruk ennå
        // liveDataImagesList.postValue(images!!)

        imageViewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        // todo: er ikke ordentlig i grid med størrelse = størrelse på bildet enda
        val gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        rvImage.layoutManager = layoutManager
        layoutManager.gapStrategy = gapStrategy
        rvImage.adapter = ResultsAdapter(this, images = ArrayList<ImageModelResultItem>(images!!), this)


        btnDeleteList?.setOnClickListener{
            goesHomeAndClearOldSearch(images)
        }
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
             Log.i(TAG, "Image added to database")
         } catch (e: Exception) {
             Log.e(TAG, "Crash in saving to database", e)
         }
     }

    override fun onImageClick(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Hi!")
            .setMessage("Add image to database?")
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