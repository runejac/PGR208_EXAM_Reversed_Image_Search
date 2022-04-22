package com.example.eksamen_pgr208.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.eksamen_pgr208.R
import com.example.eksamen_pgr208.data.api.ImageModelResultItem
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.image_rv_layout.view.*

class ResultsAdapter(val context: Context?,
                     private val images: ArrayList<ImageModelResultItem>,
                     private val listener: RecyclerClick
                    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_rv_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        Glide.with(context!!.applicationContext).load(images[position].image_link)
            .fitCenter()
            .transform(RoundedCorners(30))
            .into(holder.itemView.image_result)


    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(v: View?): RecyclerView.ViewHolder(v!!), View.OnClickListener{
        val image : ImageView = this.itemView.image_result

        override fun onClick(v: View?) {
            val position : Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onImageClick(position)

                // todo ordne enlarge image enten her eller på line 113 i ResultActivity.kt
                /*Glide.with(context!!.applicationContext)
                    .load(position)
                    .override(700, 400)
                    .fitCenter()
                    .into(itemView.image_result)*/

            }
        }

        init {
            image.setOnClickListener(this)
        }

        val imageResult = itemView.image_result!!
    }

    interface RecyclerClick{
        fun onImageClick(position: Int)
    }

}