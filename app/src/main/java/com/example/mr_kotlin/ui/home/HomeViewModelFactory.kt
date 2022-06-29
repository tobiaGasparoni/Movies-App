package com.example.mr_kotlin.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mr_kotlin.data.movie.MovieRepository
import com.example.mr_kotlin.data.user.UserRepository
import com.example.mr_kotlin.utils.SensorAdmin
import com.example.mr_kotlin.utils.auth.Auth

// The same repository that's needed for QuotesViewModel
// is also passed to the factory
class HomeViewModelFactory(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository,
    private val sensorAdmin: SensorAdmin,
    private val auth: Auth
    )
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepository, movieRepository, sensorAdmin, auth) as T
    }
}