package com.example.mr_kotlin.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mr_kotlin.data.genre.GenreRepository
import com.example.mr_kotlin.data.user.UserRepository
import com.example.mr_kotlin.ui.home.HomeViewModel
import com.example.mr_kotlin.utils.SensorAdmin
import com.example.mr_kotlin.utils.auth.Auth

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val genreRepository: GenreRepository,
    private val sensorAdmin: SensorAdmin,
    private val auth: Auth
    )
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(userRepository,genreRepository, sensorAdmin, auth) as T
    }


}