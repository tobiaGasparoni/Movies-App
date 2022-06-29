package com.example.mr_kotlin.data.MovieUpcoming

import retrofit2.Response

class MovieUpcomingRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun getUpcoming() = retrofitService.getUpcoming()

}