package com.example.eksamen_pgr208.data

import androidx.room.Entity
import androidx.room.PrimaryKey


// Alle filer under data-folderen er skrevet ved hjelp av denne videoen:
// https://www.youtube.com/watch?v=lwAvI3WDXBY
// anbefaler å se den

@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val thumbnail_link: String,
    val image_link: String
)