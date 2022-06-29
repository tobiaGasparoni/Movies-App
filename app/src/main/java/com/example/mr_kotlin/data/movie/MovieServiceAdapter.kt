package com.example.mr_kotlin.data.movie

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mr_kotlin.data.genre.GenrePieChart
import com.example.mr_kotlin.data.movie.MoviedLiked.MovieLiked
import com.example.mr_kotlin.utils.FirebaseFunctionsManager
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONObject

class MovieServiceAdapter {

    // A fake database table
    private val movieList = mutableListOf<Movie>()

    // MutableLiveData is from the Architecture Components Library
    // LiveData can be observed for changes
    private val movies = MutableLiveData<Array<Movie>>()

    private val reMovies: MutableLiveData<ArrayList<Movie>> = MutableLiveData<ArrayList<Movie>>()


    init {
        // Immediately connect the now empty quoteList
        // to the MutableLiveData which can be observed
//        movies.value = movieList
    }

    fun addMovie(movie: Movie) {
        movieList.add(movie)
        // After adding a quote to the "database",
        // update the value of MutableLiveData
        // which will notify its active observers
//        movies.value = movieList
    }

    // Casting MutableLiveData to LiveData because its value
    // shouldn't be changed from other classes
    fun getMovies() = movies as LiveData<List<Movie>>

    fun getMovie(movieId:String) {}

    suspend fun getRecommendation(email:String, movieIds:String):LiveData<ArrayList<Movie>>{

        Firebase.crashlytics.log("ERROR_MovieDao_GetRecomendation")



        val col = "CO"
        val data: JSONObject = JSONObject(
            "{" +
                    "userEmail: $email," +
                    "moviesIds: [$movieIds]," +
                    "countryId: $col" +
                    "}"
        )

        val moviesArray: JSONArray = FirebaseFunctionsManager.callFunctionArray("getRecordMock",data )
        val result = ArrayList<Movie>()
        var moJso:JSONObject
        var movie: Movie

        for ( i in 0 until moviesArray.length()){
            moJso = moviesArray.getJSONObject(i)
            movie =Movie (
                moJso["id"].toString().toInt(),
                moJso["title"].toString(),
                moJso["tmdbId"].toString().toInt(),
                moJso["adult"].toString().toBoolean(),
                moJso["ratingScore"].toString().toDouble(),
                moJso["posterComplete"].toString(),
                moJso["runTime"].toString().toInt(),
                moJso["overview"].toString(),
                moJso["budget"].toString().toInt(),
                moJso["numLikes"].toString().toInt()
            )
            result.add(movie)
        }
        reMovies.value=result
        return reMovies
    }

    suspend fun likeRecommendation(email: String, movieId: String){
        Firebase.crashlytics.log("ERROR_MovieDao_LikeRecommendation")

        val data: JSONObject = JSONObject(
            "{" +
                    "userEmail: $email," +
                    "movieId: $movieId" +
                    "}"
        )
        try {
            FirebaseFunctionsManager.callFunctionObject("likeRecomendation",data)

        }catch (e: Exception){}
    }


    suspend fun getTrend(numMovies: String): ArrayList<MovieTrend> {

        var moviesTrend: ArrayList<MovieTrend> = ArrayList<MovieTrend>()
        //Log.d(TAG, "METODO ENTRO AL GET TREND")

        val moviesArray : JSONArray = FirebaseFunctionsManager.callFunctionArray("getTrends",20)
        //Log.d(TAG, "METODO GET TREND"+ moviesArray)

        var moJso:JSONObject
        var movieTrend: MovieTrend

        for ( i in 0 until moviesArray.length()){
            moJso = moviesArray.getJSONObject(i)

            movieTrend = MovieTrend(
                moJso["posterComplete"].toString(),
                moJso["title"].toString(),
                moJso["runTime"].toString().toInt(),
                moJso["numLikes"].toString().toInt(),
                moJso["ratingScore"].toString().toDouble(),
            )
            moviesTrend.add(movieTrend)
        }
        //Log.d(TAG, "TREND"+ moviesTrend.toString())
        return moviesTrend
    }

    suspend fun getLikedMovies(userEmail: String): ArrayList<MovieLiked> {

        var moviesLiked: ArrayList<MovieLiked> = ArrayList<MovieLiked>()
        var LikedList = "LikedList"
        val partialUser: JSONObject = JSONObject(
            hashMapOf(
                "userEmail" to userEmail,
                "name" to LikedList
            ) as Map<String, String>
        )
        val movieObject : JSONObject = FirebaseFunctionsManager.callFunctionObject("getList",partialUser)
       // Log.d(TAG, "22222 GET LIKE"+ movieObject)
        var a = movieObject.toString()
        val obj = JSONObject(a)
        val moviesArray: JSONArray = obj.getJSONArray("movies")
        var moJso:JSONObject
        var bool: Boolean
        var movieLiked: MovieLiked

        for ( i in 0 until moviesArray.length()){
            moJso = moviesArray.getJSONObject(i)
            bool = false
            if(moJso["adult"] as Boolean) bool = true

            movieLiked = MovieLiked(
                moJso["posterComplete"].toString(),
                moJso["title"].toString(),
                moJso["releaseDate"].toString(),
                moJso["runTime"].toString().toInt(),
                bool,
                moJso["ratingScore"].toString().toDouble()
            )
            moviesLiked.add(movieLiked)
        }
        // Log.d(TAG, "TREND"+ moviesLiked.toString())
        return moviesLiked
    }





}