package com.example.mr_kotlin.data.movie.MoviedLiked

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(MovieLikedRoom::class), version = 1, exportSchema = false)
abstract class MovieLikedDatabase : RoomDatabase() {

    abstract fun movieLikedLocalDAO(): MovieLikedLocalDAO
}