package com.example.mr_kotlin.ui.profile

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mr_kotlin.data.genre.GenrePieChart
import com.example.mr_kotlin.data.genre.GenreServiceAdapter
import com.example.mr_kotlin.data.genre.GenreRepository
import com.example.mr_kotlin.data.user.Picture
import com.example.mr_kotlin.data.user.User
import com.example.mr_kotlin.data.user.LruChacheUser
import com.example.mr_kotlin.data.user.UserServiceAdapter
import com.example.mr_kotlin.data.user.UserRepository
import com.example.mr_kotlin.utils.SensorAdmin
import com.example.mr_kotlin.utils.auth.Auth
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository.getInstance(UserServiceAdapter(), LruChacheUser()),
    private val genreRepository: GenreRepository = GenreRepository.getInstance(GenreServiceAdapter()),
    private val sensorAdmin: SensorAdmin,
    private val auth: Auth,
    )
    : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userPic = MutableLiveData<Picture>()
    val userPic: LiveData<Picture> = _userPic

    private val _userStats = MutableLiveData<ArrayList<GenrePieChart>>()
    val userStats: LiveData<ArrayList<GenrePieChart>> = _userStats

    init {
        viewModelScope.launch {
            _user.value = getCurrentUser()!!.email?.let { userRepository.getUser(it) }
            _userPic.value = getCurrentUser()!!.email?.let { userRepository.getUserPicture(it) }
            _userStats.value = getCurrentUser()!!.email?.let { genreRepository.getUserStats(it) }
        }
    }

    fun getCurrentUser() = auth.getCurrentUser()




    /***********************************************************************************************
     * Sensor methods
     **********************************************************************************************/
    fun initializeSensorListener(ProfileActivity: ProfileActivity) =
        sensorAdmin.initializeSensorListener(ProfileActivity)

    fun registerListener(currentActivity: AppCompatActivity) = sensorAdmin.registerListener(currentActivity)
    fun unregisterListener(currentActivity: AppCompatActivity) = sensorAdmin.unregisterListener(currentActivity)

}