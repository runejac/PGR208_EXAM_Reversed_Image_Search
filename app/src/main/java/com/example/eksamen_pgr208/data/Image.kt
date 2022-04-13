package com.example.eksamen_pgr208.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val thumbnail_link: String,
    val image_link: String
)