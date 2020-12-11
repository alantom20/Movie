package com.home.movies.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity
class Record(var poster : String,
             var backdrop : String,
             @NotNull
             var title : String,
             var date : String,
             var vote : Double,
             var star : Boolean,
             var info : String,
             @PrimaryKey
             var id :Int) {
}
