package com.example.mr_kotlin.data.user

import android.graphics.Bitmap
import androidx.collection.LruCache

class LruChacheUser {

    private object HOLDER {
        val INSTANCE = LruChacheUser()
    }

    companion object {
        val instance: LruChacheUser by lazy { HOLDER.INSTANCE }
    }

    val lru: LruCache<Any, Any>

    init {

        lru = LruCache(1024)

    }

    fun saveBitmapToCahche(key: String, bitmap: Bitmap?) {

        try {
            if (bitmap != null) {
                LruChacheUser.instance.lru.put(key, bitmap)
            }
        } catch (e: Exception) {
        }

    }

    fun saveBitmapToString(key: String, bitmap: String) {

        try {
            if (bitmap != null) {
                LruChacheUser.instance.lru.put(key, bitmap)
            }
        } catch (e: Exception) {
        }

    }

    fun retrieveStringFromCache(key: String): String {

        try {
            return LruChacheUser.instance.lru.get(key) as String
        } catch (e: Exception) {
        }

        return " "
    }

    fun retrieveBitmapFromCache(key: String): Bitmap? {

        try {
            return LruChacheUser.instance.lru.get(key) as Bitmap?
        } catch (e: Exception) {
        }

        return null
    }



}