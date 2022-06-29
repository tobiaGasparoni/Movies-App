package com.example.mr_kotlin.utils

import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception


/***************************************************************************************************
 *
 **************************************************************************************************/
class Utils private constructor () {

    // Here we implement the use of the singleton design pattern.
    // There will only be one Utils entry point to access utility functions.
    companion object {
        /*******************************************************************************************
         *
         ******************************************************************************************/
        fun showSnackbar(activity: AppCompatActivity, message: String) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }
}
