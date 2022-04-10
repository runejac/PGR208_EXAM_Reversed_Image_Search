package com.example.eksamen_pgr208

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eksamen_pgr208.adapters.ResultsAdapter
import com.example.eksamen_pgr208.data.api.ImageModelResultItem
import kotlinx.android.synthetic.main.result_activity.*
import java.util.ArrayList

class ResultActivity : AppCompatActivity() {
    private lateinit var rvImage : RecyclerView
    private var liveDataImagesList : MutableLiveData<ArrayList<ImageModelResultItem>> = MutableLiveData<ArrayList<ImageModelResultItem>>()
    private var btnDeleteList : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        val images: ArrayList<ImageModelResultItem>? = intent.getParcelableArrayListExtra("images")
        rvImage = rv_results
        btnDeleteList = btn_delete_list

        // livedata ikke i bruk ennå
        liveDataImagesList.postValue(images!!)


        rvImage.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        rvImage.adapter = ResultsAdapter(this, images = ArrayList<ImageModelResultItem>(images!!))

        btnDeleteList?.setOnClickListener{
            goesHomeAndClearOldSearch(images)
        }
    }

    private fun goesHomeAndClearOldSearch(list: ArrayList<ImageModelResultItem>) {
        list.clear()
        startActivity(Intent(this, MainActivity::class.java))
    }
}