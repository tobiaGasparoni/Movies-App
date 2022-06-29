package com.example.mr_kotlin.ui.home

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.mr_kotlin.data.genre.GenrePieChart
import com.example.mr_kotlin.data.user.Picture
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mr_kotlin.data.movie.MovieRepository
import com.example.mr_kotlin.data.movie.MovieTrend
import com.example.mr_kotlin.data.user.User
import com.example.mr_kotlin.data.user.UserRepository
import com.example.mr_kotlin.utils.SensorAdmin
import com.example.mr_kotlin.utils.auth.Auth
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository,
    private val sensorAdmin: SensorAdmin,
    private val auth: Auth
) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userPic = MutableLiveData<Picture>()
    val userPic: LiveData<Picture> = _userPic

    init {
        if (getCurrentAuthUser() != null) {
            viewModelScope.launch {
                _user.value = getCurrentAuthUser()!!.email?.let { userRepository.getUser(it) }
            }
            viewModelScope.launch {
                _userPic.value = getCurrentAuthUser()!!.email?.let { userRepository.getUserPicture(it) }
            }
        }
    }

    /***********************************************************************************************
     * User methods
     **********************************************************************************************/

    fun getCurrentAuthUser() = auth.getCurrentUser()

    /***********************************************************************************************
     * Sensor methods
     **********************************************************************************************/
    fun initializeSensorListener(homeActivity: HomeActivity) =
        sensorAdmin.initializeSensorListener(homeActivity)

    fun registerListener(homeActivity: HomeActivity) = sensorAdmin.registerListener(homeActivity)

    fun unregisterListener(homeActivity: HomeActivity) = sensorAdmin.unregisterListener(homeActivity)




    /***********************************************************************************************
     * Get trends Methods
     **********************************************************************************************/

    private val _moviesTrend = MutableLiveData<ArrayList<MovieTrend>>()

    val MovieTrend: LiveData<ArrayList<MovieTrend>> = _moviesTrend

    init {
        viewModelScope.launch {

            _moviesTrend.value = movieRepository.getTrends("10")
        }
    }




}