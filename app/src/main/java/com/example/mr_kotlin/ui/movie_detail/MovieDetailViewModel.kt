package com.example.mr_kotlin.ui.movie_detail

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.mr_kotlin.data.movie.MovieServiceAdapter
import com.example.mr_kotlin.data.movie.MovieRepository
import com.example.mr_kotlin.utils.SensorAdmin
import com.example.mr_kotlin.utils.auth.Auth


class MovieDetailViewModel (
    private val movieRepository: MovieRepository = MovieRepository.getInstance(MovieServiceAdapter()),
    private val sensorAdmin: SensorAdmin,
    private val auth: Auth
)
    : ViewModel() {

    /***********************************************************************************************
     * Movie methods
     **********************************************************************************************/
    fun getMovie(movieId:String) = movieRepository.getMovie(movieId)

    suspend fun getRecommendation(email:String, movieIds:String) = movieRepository.getRecommendation(email, movieIds)

    suspend fun likeRecommendation(email:String, movieId:String) = movieRepository.likeRecommendation(email,movieId)

    fun getCurrentAuthUser() = auth.getCurrentUser()

    /***********************************************************************************************
     * Sensor methods
     **********************************************************************************************/
    fun initializeSensorListener(movieDetailActivity: MovieDetailActivity) =
            sensorAdmin.initializeSensorListener(movieDetailActivity)

    fun registerListener(currentActivity: AppCompatActivity) = sensorAdmin.registerListener(currentActivity)

    fun unregisterListener(currentActivity: AppCompatActivity) = sensorAdmin.unregisterListener(currentActivity)
}