package com.example.mr_kotlin.data.genre

import androidx.room.*

@Dao
interface GenreDAO {

    //Consulta
    @Query("SELECT a.PiechartMap FROM GenreLocalDB as a")
    fun getPiechartValues(): List<GenreDB>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: GenreDB)

    @Query("DELETE FROM GenreLocalDB")
    suspend fun deleteAll()


}