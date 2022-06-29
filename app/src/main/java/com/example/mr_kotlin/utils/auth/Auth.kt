package com.example.mr_kotlin.utils.auth;

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.ui.signup.SignupViewModel
import com.example.mr_kotlin.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/***************************************************************************************************
 *
 *
 * @constructor
 **************************************************************************************************/
class Auth{

    /***********************************************************************************************
     * Attribute that allows the access to the Firebase functions
     **********************************************************************************************/
    private var firebaseAuth: FirebaseAuth = Firebase.auth

    /***********************************************************************************************
     * Gets the currently logged in user.
     *
     * @return null if no user is logged in or the FirebaseUser object.
     **********************************************************************************************/
    fun getCurrentUser(): FirebaseUser? {
        // Initialize Firebase Auth
        return firebaseAuth.currentUser
    }

    /***********************************************************************************************
     * Connects to the Firebase authentication system to create the new user.
     * With the user ID returned by the FB Authentication system, a new User document is created
     * in the Firestore Database with the initialized info.
     *
     * @return The UID of the newly created user
     **********************************************************************************************/
    suspend fun createNewAccount(signupViewModel: SignupViewModel,
                         signupActivity: Activity,
                         name: String,
                         email: String,
                         password: String,
                         birthday: String) {
        try {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
                .user
        } catch (e: FirebaseAuthUserCollisionException) {
            throw e
        }
    }

    suspend fun deleteAccount() {
        firebaseAuth.currentUser!!
            .delete()
            .await()
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    fun logout() {
        firebaseAuth.signOut()
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    suspend fun login(email: String, password: String) {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()
                // Throws exception if firebaseUser is null
            .user ?: throw Exception("Could not login to the account with email $email.")
    }
}
