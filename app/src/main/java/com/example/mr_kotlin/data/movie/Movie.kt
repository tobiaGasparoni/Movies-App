package com.example.mr_kotlin.data.movie

import java.util.*

/***************************************************************************************************
 * This constructor is used to retrieve the movies info from the database.
 **************************************************************************************************/
class Movie (
    /***********************************************************************************************
     * Attribute that contains the movie id
     **********************************************************************************************/
    private var movieId: Int,

    /***********************************************************************************************
     * Attribute that contains the title of the movie
     **********************************************************************************************/
    private var title: String,

    /***********************************************************************************************
     * Attribute that contains id
     **********************************************************************************************/
    private var tmdbId: Int,

    /***********************************************************************************************
     * Attribute that determines if the movie was made for adults
     **********************************************************************************************/
    private var adult: Boolean,

    /***********************************************************************************************
     * Attribute that contains rating score
     **********************************************************************************************/
    private var ratingScore: Double,

    /***********************************************************************************************
     * Attribute that contains url image
     **********************************************************************************************/
    private var posterComplete: String,

    /***********************************************************************************************
     * Attribute that contains runtime
     **********************************************************************************************/
    private var runTime: Int,

    /***********************************************************************************************
     * Attribute that contains realease dato
     **********************************************************************************************/
//    private var releaseDate: Date,

    /***********************************************************************************************
     * Attribute that contains overview
     **********************************************************************************************/
    private var overview: String,

    /***********************************************************************************************
     * Attribute that contains budget
     **********************************************************************************************/
    private var budget: Int,

    /***********************************************************************************************
     * Attribute that contains numlikes.
     **********************************************************************************************/
    private var numLikes:Int

    ) {

    /***********************************************************************************************
     * Next are all the getters for the attributes of this class
     **********************************************************************************************/

    fun getMovieId(): Int {
        return movieId
    }
    fun getTitle(): String {
        return title
    }
    fun getTMDBId(): Int {
        return tmdbId
    }
    fun getAdult(): Boolean {
        return adult
    }
    fun getRatingScore(): Double {
        return ratingScore
    }
    fun getPosterComplete(): String {
        return posterComplete
    }
    fun getRunTime(): Int {
        return runTime
    }
//    fun getReleaseDate(): Date {
//        return releaseDate
//    }
    fun getOverview(): String {
        return overview
    }
    fun getBudget(): Int {
        return budget
    }
    fun getNumLikes(): Int{
        return numLikes
    }

    /***********************************************************************************************
     * The only setter that is present in this class is related to the likes because they are a key
     * component of the business. The other attributes should not be changed because they make an
     * integral part of the movie's identity.
     **********************************************************************************************/
    fun setNumLikes(pNumLikes: Int){
        this.numLikes = pNumLikes
    }

}