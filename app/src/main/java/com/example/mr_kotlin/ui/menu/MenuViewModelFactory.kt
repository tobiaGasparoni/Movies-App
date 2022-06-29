package com.example.mr_kotlin.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mr_kotlin.utils.auth.Auth

class MenuViewModelFactory (
    private val auth: Auth
)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuViewModel(auth) as T
    }
}