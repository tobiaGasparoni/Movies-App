package com.example.mr_kotlin.data.genre

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mr_kotlin.utils.FirebaseFunctionsManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class GenreServiceAdapter {

    //Use to take the firebase functions
    private lateinit var functions: FirebaseFunctions

    // A fake database table
    private val genreList = mutableListOf<Genre>()


    // MutableLiveData is from the Architecture Components Library
    // LiveData can be observed for changes
    private val genres = MutableLiveData<List<Genre>>()

    private var userString: String = " "

    private val respGenre: MutableLiveData<ArrayList<GenrePieChart>> = MutableLiveData<ArrayList<GenrePieChart>>()



/*    fun addGenre(genre: Genre) {
        genreList.add(genre)
        // After adding a quote to the "database",
        // update the value of MutableLiveData
        // which will notify its active observers
        genres.value = genreList
    }*/

    suspend fun getUserStats(email: String): ArrayList<GenrePieChart>{

        Firebase.crashlytics.log("ERROR_GenreDAO_GetUserStats")

        val partialUser= JSONObject(
            hashMapOf(
                "email" to email
            ) as Map<String, String>
        )
        val userObject: JSONObject = FirebaseFunctionsManager.callFunctionObject(
            "getUser",
            partialUser
        )

        val a = userObject.toString()
        val obj = JSONObject(a)
        val employee: JSONArray = obj.getJSONArray("stats")
        val result = ArrayList<GenrePieChart>()

        for ( i in 0 until employee.length()){
            val moJso = employee.getJSONObject(i)
            val GenreNameArray = moJso.getJSONObject("genre")
            val genrename = GenreNameArray.get("name")

            val genres =GenrePieChart(
                moJso["genreId"].toString().toInt(),
                moJso["amount"].toString().toInt(),
                genrename.toString(),
                moJso["userEmail"].toString()
            )
            result.add(genres)
        }
        return result
    }



}

