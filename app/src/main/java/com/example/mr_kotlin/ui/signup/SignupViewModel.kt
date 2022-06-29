package com.example.mr_kotlin.ui.signup

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.mr_kotlin.data.user.User
import com.example.mr_kotlin.data.user.UserServiceAdapter
import com.example.mr_kotlin.utils.auth.Auth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignupViewModel (
    private val auth: Auth,
    private val userServiceAdapter: UserServiceAdapter
)
    : ViewModel() {

    /***********************************************************************************************
     * User methods
     **********************************************************************************************/
    fun getCurrentAuthUser() = auth.getCurrentUser()

    suspend fun createNewAuthUser(signupActivity: AppCompatActivity,
                                  name: String,
                                  email: String,
                                  password: String,
                                  birthday: String) {
        try {
            auth.createNewAccount(
                this,
                signupActivity,
                name,
                email,
                password,
                birthday
            )
        } catch (e: FirebaseAuthUserCollisionException) {
            throw e
        }
    }

    suspend fun createNewDBUser(name: String,
                                email: String,
                                birthday: String) {
        userServiceAdapter.save(User(name, email, birthday))
    }

}