package com.example.eksamen_pgr208.data.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageModelResultItem(
    val current_date: String,
    val description: String,
    val domain: String,
    val identifier: String,
    val image_link: String,
    val name: String,
    val store_link: String,
    val thumbnail_link: String,
    val tracking_id: String
) : Parcelable


