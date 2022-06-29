package com.example.mr_kotlin.data.movie

import android.content.ContentValues
import android.util.Log
import com.example.mr_kotlin.data.movie.MoviedLiked.MovieLiked

// FakeQuoteDao must be passed in - it is a dependency
// You could also instantiate the DAO right inside the class without all the fuss, right?
// No. This would break testability - you need to be able to pass a mock version of a DAO
// to the repository (e.g. one that upon calling getQuotes() returns a dummy list of quotes for testing)
// This is the core idea behind DEPENDENCY INJECTION - making things completely modular and independent.
class MovieRepository private constructor(private val movieServiceAdapter: MovieServiceAdapter) {

    // This may seem redundant.
    // Imagine a code which also updates and checks the backend.
    fun addMovie(movie: Movie) {
        movieServiceAdapter.addMovie(movie)
    }

    fun getMovies() = movieServiceAdapter.getMovies()

    fun getMovie(movieId:String) = movieServiceAdapter.getMovie(movieId)

    suspend fun likeRecommendation(email: String, movieId: String) = movieServiceAdapter.likeRecommendation(email, movieId)

    suspend fun getRecommendation(email:String, movieIds:String)= movieServiceAdapter.getRecommendation(email,movieIds )

    suspend fun getTrends(numMovies: String): ArrayList<MovieTrend> {

     return  movieServiceAdapter.getTrend(numMovies)
    }


    suspend fun getLikedMovies(userEmail: String): ArrayList<MovieLiked> {

        // Log.d(ContentValues.TAG, "MOVIE REPOSITORY"+ userEmail)

        return  movieServiceAdapter.getLikedMovies(userEmail)
    }




    companion object {
        // Singleton instantiation you already know and love
        @Volatile private var instance: MovieRepository? = null

        fun getInstance(movieServiceAdapter: MovieServiceAdapter) =
            instance ?: synchronized(this) {
                instance ?: MovieRepository(movieServiceAdapter).also { instance = it }
            }
    }
}