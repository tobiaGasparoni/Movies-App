package com.example.mr_kotlin.utils

import android.app.Application
import com.example.mr_kotlin.data.user.PrefUser

class UserApplication: Application() {

    companion object{
        lateinit var  pref: PrefUser
    }

    override fun onCreate(){
        super.onCreate()
        pref = PrefUser(applicationContext)
    }


}