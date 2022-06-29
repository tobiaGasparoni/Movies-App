package com.example.mr_kotlin.data.user

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mr_kotlin.utils.FirebaseFunctionsManager
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

class UserServiceAdapter {

    suspend fun save(pNewUser: User) {

        val newUser = JSONObject(
            hashMapOf(
                "email" to pNewUser.getEmail(),
                "name" to pNewUser.getName(),
                "birthday" to pNewUser.getName(),
                "avgPassBeforeLike" to 0.0
            ) as Map<String, Any>
        )

        val userObject: JSONObject = FirebaseFunctionsManager.callFunctionObject(
            "createUser",
            newUser
        )

        println("RESULT OF USER CREATED ::::: $userObject")
    }


    suspend fun updateUser(pNewUser: UserEdit) {

        val newUser = JSONObject(
            hashMapOf(
                "email" to pNewUser.getEmail(),
                "image" to pNewUser.getEmail(),
                "name" to pNewUser.getName(),
                "birthday" to pNewUser.getName()
            ) as Map<String, String>
        )


        val userObject: JSONObject = FirebaseFunctionsManager.callFunctionObject(
            "updateUser",
            newUser
        )

        println("RESULT OF USER CREATED ::::: $userObject")
    }




    suspend fun load(email: String): User {
        Firebase.crashlytics.log("ERROR_UserDAO_LoadUser")

        val partialUser: JSONObject = JSONObject(
            hashMapOf(
                "email" to email
            ) as Map<String, String>
        )

        val userObject: JSONObject = FirebaseFunctionsManager.callFunctionObject(
            "getUser",
            partialUser
        )

        return User(
            userObject["name"].toString(),
            userObject["email"].toString(),
            userObject["birthday"].toString()
        )
    }

    suspend fun loadPicture(email: String): Picture {

        Firebase.crashlytics.log("ERROR_UserDAO_LoadPicture")

        val partialUser: JSONObject = JSONObject(
            hashMapOf(
                "email" to email
            ) as Map<String, String>
        )

        val userObject: JSONObject = FirebaseFunctionsManager.callFunctionObject(
            "getUser",
            partialUser
        )

        return Picture (
            userObject["email"].toString(),
            userObject["image"].toString()
        )
    }
}