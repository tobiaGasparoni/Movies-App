package com.example.mr_kotlin.data.user

import android.content.Context

class PrefUser(val context:Context) {

    private val SHARED_NAME ="Mydb"
    private val USER_NAME="username"
    private val USER_EMAIL="useremail"
    private val USER_BIRTHDAY="userbirthday"

    fun getName():String{
        val storage = context.getSharedPreferences(SHARED_NAME, 0)
        return storage.getString(USER_NAME, "")!!
    }

    fun setName(name:String){
        val storage = context.getSharedPreferences(SHARED_NAME, 0)
        storage.edit().putString(USER_NAME, name).apply()
    }

    fun getEmail():String{
        val storage = context.getSharedPreferences(SHARED_NAME, 0)
        return storage.getString(USER_EMAIL, "")!!
    }

    fun setEmail(email:String){
        val storage = context.getSharedPreferences(SHARED_NAME, 0)
        storage.edit().putString(USER_EMAIL, email).apply()
    }

    fun getBirthday():String{
        val storage = context.getSharedPreferences(SHARED_NAME, 0)
        return storage.getString(USER_BIRTHDAY, "")!!
    }

    fun setBirthday(birthday:String){
        val storage = context.getSharedPreferences(SHARED_NAME, 0)
        storage.edit().putString(USER_BIRTHDAY, birthday).apply()
    }

}