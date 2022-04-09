package com.example.eksamen_pgr208.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ResultsAdapter(val image: List<String>) : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    inner class ResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                //TODO: ( Her skal man kalle på Layout resource filen. ImageView )

            )
        )
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}