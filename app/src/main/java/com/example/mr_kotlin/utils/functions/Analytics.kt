package com.example.mr_kotlin.utils.functions

import android.content.ContentValues
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_kotlin.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class Analytics {

    /***********************************************************************************************
     *
     **********************************************************************************************/
    private lateinit var functions: FirebaseFunctions


    /***********************************************************************************************
     *
     **********************************************************************************************/
    private fun postDurationFunctionFirebase(funcName: String, duration: Long): Task<Object> {
        functions = Firebase.functions

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "funcName" to funcName,
            "time" to duration
        )

        return functions
            .getHttpsCallable("recordTime")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as Object
                result
            }
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    fun postDurationFunction(activity: AppCompatActivity, funcName: String, duration: Long) {
        postDurationFunctionFirebase(funcName, duration)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                    }

                    Log.w(ContentValues.TAG, "addMessage:onFailure", e)
                    Utils.showSnackbar(activity, "An error occurred.")
                    return@OnCompleteListener
                }

                println("Transaction Result ::::: ${task.result}")
                /*for(obj in task.result!!.iterator())
                    println("Transaction Result ::::: $obj")*/
            })
    }

}