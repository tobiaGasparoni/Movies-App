package com.example.mr_kotlin.utils

import android.content.ContentValues
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject

class FirebaseFunctionsManager {
    companion object {

        /*******************************************************************************************
         *
         ******************************************************************************************/
        private lateinit var functions: FirebaseFunctions

        /*******************************************************************************************
         *
         ******************************************************************************************/
        suspend fun callFunctionObject(funName: String,
                                       data: Any): JSONObject {
            val response: Any? = callFunctionFirebase(funName, data)
            return if(response != null) {
                JSONObject(response as Map<String, Any>)
            } else {
                throw Exception()
            }
        }

        /*******************************************************************************************
         *
         ******************************************************************************************/
        suspend fun callFunctionArray(funName: String,
                                      data: Any): JSONArray {
            val response: Any? = callFunctionFirebase(funName, data)
            return if(response != null) {
                JSONArray(response as ArrayList<Map<String, Any>>)
            } else {
                throw Exception()
            }
        }


        suspend fun callFunctionFirebaseAny(funName: String, data: Any): Any? {

            return try {
                functions = Firebase.functions
                functions
                    .getHttpsCallable(funName)
                    .call(data)
                    .await()
                    .data
            } catch (e: Exception) {
                throw e
            }
        }

        /*******************************************************************************************
         *
         ******************************************************************************************/
        private suspend fun callFunctionFirebase(funName: String, data: Any): Any? {

            return try {
                functions = Firebase.functions
                functions
                    .getHttpsCallable(funName)
                    .call(data)
                    .await()
                    .data
            } catch (e: Exception) {
                throw e
            }
        }
    }
}