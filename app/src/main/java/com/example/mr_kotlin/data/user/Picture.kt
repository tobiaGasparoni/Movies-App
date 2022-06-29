package com.example.mr_kotlin.data.user

class Picture(

    private var email: String,

    private var image: String,

) {

    /***********************************************************************************************
     * @return the user's name
     **********************************************************************************************/
    fun getemail(): String {
        return email
    }

    /***********************************************************************************************
     * @return the user's email
     **********************************************************************************************/
    fun getimage(): String {
        return image
    }
}