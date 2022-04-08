package com.example.eksamen_pgr208.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


// Alle filer under data-folderen er skrevet ved hjelp av denne videoen:
// https://www.youtube.com/watch?v=lwAvI3WDXBY
// anbefaler å se den

@Database(entities = [Image::class], version = 1)
abstract class ImageDatabase: RoomDatabase() {

    abstract fun imageDao(): ImageDao

    companion object{
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getDataBase(context: Context): ImageDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java,
                    "image_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}