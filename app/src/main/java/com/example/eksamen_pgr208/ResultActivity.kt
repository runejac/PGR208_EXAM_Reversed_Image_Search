package com.example.eksamen_pgr208

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eksamen_pgr208.adapters.ResultsAdapter
import com.example.eksamen_pgr208.data.api.ImageModelResultItem
import kotlinx.android.synthetic.main.image_rv_layout.*
import kotlinx.android.synthetic.main.result_activity.*
import java.util.ArrayList

class ResultActivity : AppCompatActivity() {
    private lateinit var rvImage : RecyclerView
    private var btnDeleteList : Button? = null
    private var imageResult : ImageButton? = null

    // private lateinit var imageViewModel: ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)

        val images: ArrayList<ImageModelResultItem>? = intent.getParcelableArrayListExtra("images")
        rvImage = rv_results
        btnDeleteList = btn_delete_list
        imageResult = image_result


        // livedata ikke i bruk ennå
        // liveDataImagesList.postValue(images!!)

        // imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        rvImage.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        rvImage.adapter = ResultsAdapter(this, images = ArrayList<ImageModelResultItem>(images!!))

        btnDeleteList?.setOnClickListener{
            goesHomeAndClearOldSearch(images)
        }


        // Vil vi her ha det sånn at når vi klikker på et bilde, så kommer det en AlertDialog
        // med typ "Add image to database?" - Joachim
        // Anbefales visst å laste ned DB Browser for SQLite, så kan man manuelt sjekke om data lagres i db

        /*
        * private fun addToDatabase() {
        *   val thumbNailLink = imageResult.thumbnail_link
        *   val imageLink = imageResult.image_link
        *
        *   val image = Image(0, thumbNailLink, imageLink)
        *   imageViewModel.addImage(image)
        *   Toast.makeText(this, "Successfully added image to database!", Toast.LENGTH_LONG).show()
        * }
        */

        /*imageResult?.setOnClickListener {
            println("image clicked")
            Toast.makeText(this,  "Image clicked!", Toast.LENGTH_LONG).show()
            få opp en AlertDialog, trykk "Add" for addToDatabase()
        }*/

    }


    private fun goesHomeAndClearOldSearch(list: ArrayList<ImageModelResultItem>) {
        list.clear()
        startActivity(Intent(this, MainActivity::class.java))
    }

}