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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (id != other.id) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (image?.contentHashCode() ?: 0)
        return result
    }
}