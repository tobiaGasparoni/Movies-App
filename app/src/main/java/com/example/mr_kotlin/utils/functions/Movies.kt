package com.example.mr_kotlin.utils.functions

import android.content.ContentValues.TAG
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mr_kotlin.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import org.json.JSONObject

/***************************************************************************************************
 *
 **************************************************************************************************/
class Movies {
/*
    /***********************************************************************************************
     *
     **********************************************************************************************/
    private lateinit var functions: FirebaseFunctions

    /***********************************************************************************************
     *
     **********************************************************************************************/
    private fun getMovieDetailFirebase(id: Int): Task<Object> {
        functions = Firebase.functions

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "id" to id
        )

        return functions
            .getHttpsCallable("getMovie")
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
    fun getMovieDetail(activity: AppCompatActivity, id: Int) {


        getMovieDetailFirebase(id)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                    }

                    Log.w(TAG, "addMessage:onFailure", e)
                    Utils.showSnackbar(activity, "An error occurred.")
                    return@OnCompleteListener
                }
                Utils.getRecommendationList(task.result.toString())

                println("Transaction Result ::::: ${task.result}")
                /*for(obj in task.result!!.iterator())
                    println("Transaction Result ::::: $obj")*/
            })
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    private fun getMoviesFirebase(id: Int): Task<Object> {
        functions = Firebase.functions

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "id" to id
        )

        return functions
            .getHttpsCallable("getMovies")
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
    fun getMovies(activity: AppCompatActivity, id: Int) {
        getMoviesFirebase(id)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                    }

                    Log.w(TAG, "addMessage:onFailure", e)
                    Utils.showSnackbar(activity, "An error occurred.")
                    return@OnCompleteListener
                }

                println("Transaction Result ::::: ${task.result}")
                /*for(obj in task.result!!.iterator())
                    println("Transaction Result ::::: $obj")*/
            })
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    private fun getRecommendationsFirebase(id: String,pMoviesIDs:String): Task<Object> {
        functions = Firebase.functions

        // Create the arguments to the callable function.
        val data: JSONObject = JSONObject(
            "{" +
                    "id: $id," +
                    "moviesIds: [$pMoviesIDs]" +
                 "}"
        )

        return functions
            .getHttpsCallable("getRecommendations")
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
    fun getRecommendations(activity: AppCompatActivity, id: String, pMoviesIDs : String) {
        getRecommendationsFirebase(id,pMoviesIDs)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val e = task.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                    }

                    Log.w(TAG, "addMessage:onFailure", e)
                    Utils.showSnackbar(activity, "An error occurred.")
                    return@OnCompleteListener
                }

                println("Transaction Result Recommendations ::::: ${task.result}")
                /*for(obj in task.result!!.iterator())
                    println("Transaction Result ::::: $obj")*/
            })
    }

 */
}

