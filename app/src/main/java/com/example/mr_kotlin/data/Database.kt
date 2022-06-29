package com.example.mr_kotlin.data

import androidx.room.Room
import com.example.mr_kotlin.data.genre.GenreServiceAdapter
import com.example.mr_kotlin.data.movie.MovieServiceAdapter
import com.example.mr_kotlin.data.user.LruChacheUser
import com.example.mr_kotlin.data.user.UserServiceAdapter

class Database private constructor() {

    // All the DAOs go here!
    var genreDAO = GenreServiceAdapter()
        private set

    var movieDAO = MovieServiceAdapter()
        private set

    var userDAO = UserServiceAdapter()
        private set

    var lruChacheUser = LruChacheUser()
        private set






    companion object {
        // @Volatile - Writes to this property are immediately visible to other threads
        @Volatile private var instance: Database? = null

        // The only way to get hold of the Database object
        fun getInstance() =
            // Already instantiated? - return the instance
            // Otherwise instantiate in a thread-safe manner
            instance ?: synchronized(this) {
                // If it's still not instantiated, finally create an object
                // also set the "instance" property to be the currently created one
                instance ?: Database().also { instance = it }
            }
    }
}