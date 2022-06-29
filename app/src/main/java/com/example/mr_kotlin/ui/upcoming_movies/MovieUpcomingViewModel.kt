package com.example.mr_kotlin.ui.upcoming_movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mr_kotlin.data.MovieUpcoming.MovieUpcoming
import com.example.mr_kotlin.data.MovieUpcoming.MovieUpcomingRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieUpcomingViewModel constructor(
    private val repository: MovieUpcomingRepository
    )  : ViewModel() {

    val movieList = MutableLiveData<List<MovieUpcoming>>()

    val errorMessage = MutableLiveData<String>()

    suspend fun getUpcoming() {

        val response = repository.getUpcoming()

        println("RESPONSE IS ______--------- $response")

    }


}
