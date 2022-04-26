package com.example.eksamen_pgr208.data

import android.content.Context
import androidx.room.*

@Database(entities = [Image::class], version = 1)
//@TypeConverters(DataConverters::class)
abstract class ImageDatabase: RoomDatabase() {

    abstract fun imageDao(): ImageDao

    // Companion object so that we can access getDataBase() without
    // having to explicitly instantiate the ImageDatabase class
    companion object{
        // Any writes made to this variable is immediately visible to
        // other threads
        @Volatile
        private var dbInstance: ImageDatabase? = null

        fun getDataBase(context: Context): ImageDatabase{
            // Temporary variable to check if the database instance already exists
            val tmp = dbInstance
            if (tmp != null) {
                return tmp
            }
            // Everything within the synchronized block is only accessible
            // by one thread at a time, to make sure we always get the correct output
            synchronized(this) {
                val imageDbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java,
                    "image_db"
                ).build()
                dbInstance = imageDbInstance
                return imageDbInstance
            }
        }
    }

}