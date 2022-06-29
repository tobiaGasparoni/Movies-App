package com.example.mr_kotlin.data.user

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.mr_kotlin.utils.UserApplication.Companion.pref
import java.util.concurrent.Executors

class UserRepository private constructor(
    private val userServiceAdapter: UserServiceAdapter,
    private val lruChache: LruChacheUser) {

    // Imagine a code which also updates and checks the backend.
    suspend fun postUser(user: User) {
        userServiceAdapter.save(user)
    }

    suspend fun getUser(email: String): User {

        return if(pref.getName() == ""){
            Log.d(TAG, "no name")
            val user = userServiceAdapter.load(email)
            saveUserCache(user)
            user
        }else{
            Log.d(TAG, "si name")
            User(pref.getName(),pref.getEmail(),pref.getBirthday())
        }
    }

    private fun saveUserCache(user:User){
        pref.setBirthday(user.getBirthday())
        pref.setEmail(user.getEmail())
        pref.setName(user.getName())
    }

    suspend fun getUserPicture(email: String): Picture {

       //val picture: MutableLiveData<Picture> = MutableLiveData<Picture>()

        var image = userServiceAdapter.loadPicture(email)

        /*
        var email: String =" "
        var imagen: String=" "

        image.observe(currentActivity){
            email= it.getemail()
            imagen= it.getimage()


        }

        lruChache.saveBitmapToString("ProfileURl",imagen)
        Log.d("ENTRO BTIAMP", lruChache.retrieveStringFromCache("ProfileURl"))

        //lruChache.saveBitmapToCahche("ProfilePicture", createBitmap(imagen) )
        //lruChache.retrieveBitmapFromCache("ProfilePicture")*/

        return image
    }


    //TODO micro opti
    suspend private fun createBitmap(url:String): Bitmap? {
        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()

        // Initializing the image
        var image: Bitmap?= null

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {
            // Image URL
            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(url).openStream()
                image = BitmapFactory.decodeStream(`in`)
                // Only for making changes in UI
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return image
    }





    companion object {
        // Singleton instantiation
        @Volatile private var instance: UserRepository? = null

        fun getInstance(userServiceAdapter: UserServiceAdapter, lruChache:LruChacheUser) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userServiceAdapter,lruChache).also { instance = it }
            }
    }
}