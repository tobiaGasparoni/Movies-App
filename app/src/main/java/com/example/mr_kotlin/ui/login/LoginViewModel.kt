package com.example.mr_kotlin.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.utils.Utils
import com.example.mr_kotlin.utils.auth.Auth

class LoginViewModel (
    private val auth: Auth
)
    : ViewModel() {

    /***********************************************************************************************
     * User methods
     **********************************************************************************************/
    fun getCurrentAuthUser() = auth.getCurrentUser()

    suspend fun login(email: String, password: String) {
        try {
            auth.login(email, password)
        } catch(e: Exception) {
            throw e
        }
    }
}