package com.example.mr_kotlin.data.movie.MoviedLiked


data class MovieLiked(

    var posterComplete: String,
    var title: String,
    var releaseDate: String,
    var runTime: Int,
    var adult: Boolean,
    var ratingScore: Double

    )