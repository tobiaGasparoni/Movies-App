package com.example.mr_kotlin.data.movie.MoviedLiked

import androidx.room.*

@Dao
interface MovieLikedLocalDAO {

    //either interface or abstract class as we don't provide method body we just annotate
    @Insert
    fun Insert(movieLikedRoom: MovieLikedRoom?)

    @Update
    fun Update(movieLikedRoom: MovieLikedRoom?)

    @Delete
    fun Delete(movieLikedRoom: MovieLikedRoom?)

    @Query("DELETE FROM MovieLikedRoom")
    fun DeleteAllMovies()

    //updates and returns
    @get:Query("SELECT * FROM MovieLikedRoom")
    val getallMovies: List<MovieLikedRoom>

    @Query("SELECT * FROM MovieLikedRoom WHERE title LIKE :title")
    fun findBytitle(title: String): MovieLikedRoom

}