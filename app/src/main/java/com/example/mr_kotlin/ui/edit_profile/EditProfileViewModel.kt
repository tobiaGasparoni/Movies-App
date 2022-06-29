package com.example.mr_kotlin.ui.edit_profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mr_kotlin.data.movie.MovieRepository
import com.example.mr_kotlin.data.user.*
import com.example.mr_kotlin.utils.SensorAdmin
import com.example.mr_kotlin.utils.auth.Auth
import kotlinx.coroutines.launch

class EditProfileViewModel(

    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository,
    private val sensorAdmin: SensorAdmin,
    private val userServiceAdapter: UserServiceAdapter,
    private val auth: Auth

) :ViewModel(){

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userPic = MutableLiveData<Picture>()
    val userPic: LiveData<Picture> = _userPic

    init {
        viewModelScope.launch {
            _user.value = getCurrentAuthUser()!!.email?.let { userRepository.getUser(it) }
        }
        viewModelScope.launch {
            _userPic.value = getCurrentAuthUser()!!.email?.let { userRepository.getUserPicture(it) }
        }
    }


    /***********************************************************************************************
     * User methods
     **********************************************************************************************/

    fun getCurrentAuthUser() = auth.getCurrentUser()

    suspend fun updateUser(email: String,
                           imagePicture: String,
                           name: String,
                           date: String){


        Log.d(ContentValues.TAG, "HOLA: $imagePicture")
        Log.d(ContentValues.TAG, "HOLA 2: $name")
        Log.d(ContentValues.TAG, "HOLA 3: $date")
        Log.d(ContentValues.TAG, "HOLA 4: ${email}")

        return userServiceAdapter.updateUser(UserEdit(imagePicture,name,email,date))

    }


}