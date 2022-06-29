package com.example.mr_kotlin.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mr_kotlin.data.user.UserServiceAdapter
import com.example.mr_kotlin.utils.auth.Auth

class SignupViewModelFactory (
    private val auth: Auth,
    private val userServiceAdapter: UserServiceAdapter
)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignupViewModel(auth, userServiceAdapter) as T
    }
}