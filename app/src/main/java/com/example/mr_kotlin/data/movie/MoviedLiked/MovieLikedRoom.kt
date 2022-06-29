package com.example.mr_kotlin.data.movie.MoviedLiked

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MovieLikedRoom")
data class MovieLikedRoom(

    @PrimaryKey(autoGenerate = true) var id: Int=0,

    @ColumnInfo(name = "posterComplete") var posterComplete: String,

    @ColumnInfo(name = "title") var title: String,


    @ColumnInfo(name = "releaseDate") var releaseDate: String,

    @ColumnInfo(name = "runTime") var runTime: Int,

    @ColumnInfo(name = "adult") var adult: Boolean,

    @ColumnInfo(name = "ratingScore") var ratingScore: Double
)