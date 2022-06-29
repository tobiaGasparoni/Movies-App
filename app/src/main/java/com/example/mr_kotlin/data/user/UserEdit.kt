package com.example.mr_kotlin.data.user

/***************************************************************************************************
 * This constructor is used to retrieve the user's info from the database.
 **************************************************************************************************/
class UserEdit (

    private var imagePicture:String,
    /***********************************************************************************************
     * Attribute that contains both the user's name and lastname.
     **********************************************************************************************/
    private var name: String,

    /***********************************************************************************************
     * Attribute that represents the user's email.
     **********************************************************************************************/
    private var email: String,

    /***********************************************************************************************
     * Attribute that represents the user's birthday.
     **********************************************************************************************/
    private var birthday: String

    ) {

    /***********************************************************************************************
     * @return the user's name
     **********************************************************************************************/
    fun getName(): String {
        return name
    }


    fun getimagePicture(): String {
        return imagePicture
    }

    /***********************************************************************************************
     * @return the user's email
     **********************************************************************************************/
    fun getEmail(): String {
        return email
    }

    /***********************************************************************************************
     * @return the user's birthday
     **********************************************************************************************/
    fun getBirthday(): String {
        return birthday
    }

}