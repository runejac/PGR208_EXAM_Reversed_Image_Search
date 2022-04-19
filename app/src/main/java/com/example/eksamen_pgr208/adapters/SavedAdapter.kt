package com.example.eksamen_pgr208.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eksamen_pgr208.R
import kotlinx.android.synthetic.main.image_rv_layout.view.*

class SavedAdapter(val context: Context?, private var images: ArrayList<String>, private val listener: ResultsAdapter.RecyclerClick): RecyclerView.Adapter<RecyclerView.ViewHolder>()   {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.image_rv_layout, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Glide.with(context!!.applicationContext).load(images.get(position))
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
            }
        }

        init {
            image.setOnClickListener(this)
        }

        val imageResult = itemView.image_result!!
    }



    /*@SuppressLint("NotifyDataSetChanged")
    fun setData(image: List<com.example.eksamen_pgr208.data.Image>) {
        this.images = image
        notifyDataSetChanged()
    }*/

    interface RecyclerClick{
        fun onImageClick(position: Int)
    }
}
