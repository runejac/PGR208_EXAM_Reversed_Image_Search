package com.example.eksamen_pgr208.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eksamen_pgr208.R
import kotlinx.android.synthetic.main.image_rv_layout.view.*

class SavedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()   {

    private var imageList = emptyList<com.example.eksamen_pgr208.data.Image>()

    class MyViewHolder(imageView: View): RecyclerView.ViewHolder(imageView) {
        val image : ImageView = this.itemView.image_result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.image_rv_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = imageList[position]

        //TODO: Innbiller meg at dette er alt som mangler
        // for at det skal funke, eller?

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(image: List<com.example.eksamen_pgr208.data.Image>) {
        this.imageList = image
        notifyDataSetChanged()
    }
}
