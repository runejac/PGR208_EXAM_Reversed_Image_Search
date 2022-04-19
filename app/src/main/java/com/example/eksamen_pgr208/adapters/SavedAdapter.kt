package com.example.eksamen_pgr208.adapters

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

class SavedAdapter(val context: Context?): RecyclerView.Adapter<RecyclerView.ViewHolder>()   {

    private var imageList = emptyList<Image>()

    class MyViewHolder(imageView: View): RecyclerView.ViewHolder(imageView) {
        val image : ImageView = this.itemView.image_result
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.image_rv_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // Binding Views here
        /*val mPicasso: Picasso = Picasso.get()
        // fixme: gjør så bildene får en tag på seg. Rød: Network, Blå: Disk, Grønn: Memory. Kun for dev
        //mPicasso.setIndicatorsEnabled(true)
        mPicasso.load(images[position].image_link)
            .centerCrop()
            .resize(400, 600)
                // fixme: får ikke transform til å fungere
            //.transform(RoundedCorners(50))
            .into(holder.itemView.image_result)*/


        Glide.with(context!!.applicationContext).load(imageList[position])
            .fitCenter()
            .transform(RoundedCorners(30))
            .into(holder.itemView.image_result)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setData(image: ArrayList<Image>) {
        this.imageList = image
        notifyDataSetChanged()
    }
}
