package com.example.eksamen_pgr208.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.eksamen_pgr208.R
import com.example.eksamen_pgr208.data.api.ImageModelResultItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.image_rv_layout.view.*

class ResultsAdapter(val context: Context?, private val images: ArrayList<ImageModelResultItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_rv_layout, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Binding Views here

        Picasso.get().load(images[position].image_link).into(holder.itemView.image_result)
    }

    override fun getItemCount(): Int {
        return images.size
    }



    class ViewHolder(v: View?): RecyclerView.ViewHolder(v!!), View.OnClickListener{
        val image : ImageView = this.itemView.image_result
        override fun onClick(v: View?) {



        }

        init {
            itemView.setOnClickListener(this)
        }



        val imageResult = itemView.image_result!!


    }

    interface RecyclerClick{

    }


}