package com.example.eksamen_pgr208.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

// Image entity using a bytearray and saving as blob in database
@Parcelize
@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null
) : Parcelable {

}