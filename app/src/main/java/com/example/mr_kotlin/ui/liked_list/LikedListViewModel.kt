package com.example.mr_kotlin.ui.liked_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mr_kotlin.data.movie.MovieRepository
import com.example.mr_kotlin.data.movie.MoviedLiked.MovieLiked
import com.example.mr_kotlin.data.user.UserRepository
import com.example.mr_kotlin.utils.SensorAdmin
import com.example.mr_kotlin.utils.auth.Auth
import kotlinx.coroutines.launch

class LikedListViewModel(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository,
    private val sensorAdmin: SensorAdmin,
    private val auth: Auth
)
    : ViewModel() {



    fun getCurrentAuthUser() = auth.getCurrentUser()

    private val _moviesLiked = MutableLiveData<ArrayList<MovieLiked>>()

    val movie_Liked: LiveData<ArrayList<MovieLiked>> = _moviesLiked


    init {
        viewModelScope.launch {

            _moviesLiked.value = getCurrentAuthUser()!!.email?.let {movieRepository.getLikedMovies(it)}
        }
    }



}