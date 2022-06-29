package com.example.mr_kotlin.ui.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mr_kotlin.data.movie.MovieRepository
import com.example.mr_kotlin.utils.SensorAdmin
import com.example.mr_kotlin.utils.auth.Auth

class MovieDetailViewModelFactory (
        private val movieRepository: MovieRepository,
        private val sensorAdmin: SensorAdmin,
        private val auth: Auth
)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailViewModel(movieRepository, sensorAdmin, auth) as T
    }
}