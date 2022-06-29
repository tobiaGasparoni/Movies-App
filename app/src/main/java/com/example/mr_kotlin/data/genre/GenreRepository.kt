package com.example.mr_kotlin.data.genre

class GenreRepository private constructor(private val genreServiceAdapter: GenreServiceAdapter) {

    // This may seem redundant.
    // Imagine a code which also updates and checks the backend.
/*    fun addMovie(genre: Genre) {
        genreDAO.addGenre(genre)
    }*/

    suspend fun getUserStats(email: String): ArrayList<GenrePieChart> {

        //data from local storage
        //resp = getLocalDbinfo(email)

        //data from web service
        return genreServiceAdapter.getUserStats(email)
    }

    companion object {
        // Singleton instantiation you already know and love
        @Volatile private var instance: GenreRepository? = null

        fun getInstance(genreServiceAdapter: GenreServiceAdapter) =
            instance ?: synchronized(this) {
                instance ?: GenreRepository(genreServiceAdapter).also { instance = it }
            }
    }
}