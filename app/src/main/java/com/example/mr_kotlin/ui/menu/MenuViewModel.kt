package com.example.mr_kotlin.ui.menu

import androidx.lifecycle.ViewModel
import com.example.mr_kotlin.utils.auth.Auth

class MenuViewModel (
    private val auth: Auth
)
    : ViewModel() {

    /***********************************************************************************************
     * User methods
     **********************************************************************************************/
    fun getCurrentAuthUser() = auth.getCurrentUser()

    fun logout() = auth.logout()

    suspend fun deleteAccount() = auth.deleteAccount()
}