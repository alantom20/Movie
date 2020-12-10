package com.home.movies.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Record::class),version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun recordDao() : RecordDao
    companion object{
        private var instance : MovieDatabase? = null
        fun getInstance(context: Context) : MovieDatabase?{
            if(instance == null){
                instance = Room.databaseBuilder(context,
                    MovieDatabase::class.java,
                    "movie.db").build()
            }
            return instance
        }
    }
}