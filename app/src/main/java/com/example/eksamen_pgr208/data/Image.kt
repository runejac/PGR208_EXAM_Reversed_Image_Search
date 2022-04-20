package com.example.eksamen_pgr208.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Image entity using only the thumbnail_link and the image_link
// because those were the only properties we needed according to the exam text
@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val thumbnail_link: String,
    val image_link: String
)