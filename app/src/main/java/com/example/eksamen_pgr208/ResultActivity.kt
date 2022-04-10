package com.example.eksamen_pgr208

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eksamen_pgr208.adapters.ResultsAdapter
import com.example.eksamen_pgr208.data.api.ImageModelResultItem
import kotlinx.android.synthetic.main.result_activity.*
import java.util.ArrayList

class ResultActivity : AppCompatActivity() {
    private lateinit var rvImage : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        rvImage = rv_results
        val images: ArrayList<ImageModelResultItem>? = intent.getParcelableArrayListExtra("images")

        rvImage.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        rvImage.adapter = ResultsAdapter(this, images = ArrayList<ImageModelResultItem>(images))

    }
}