package com.example.mr_kotlin.ui.upcoming_movie_detail

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mr_kotlin.R

class upcoming_movie_detailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_movie_detail)

        getIncomingIntent()
    }

    private fun getIncomingIntent() {
        Log.d(ContentValues.TAG, "getIncomingIntent: checking for incoming intents.")

        if (intent.hasExtra("movie_image")
            && intent.hasExtra("title_movie")
            && intent.hasExtra("adult")
            && intent.hasExtra("releaseDate")
            && intent.hasExtra("ratingScore")) {

            val movie_image:String = intent.getStringExtra("movie_image").toString()
            val title_movie:String = intent.getStringExtra("title_movie").toString()
            val adult = intent.getStringExtra("adult").toString()
            val releaseDate = intent.getStringExtra("releaseDate").toString()
            val ratingScore = intent.getStringExtra("ratingScore").toString()

            SetVariables(movie_image, title_movie,adult ,releaseDate, ratingScore )
        }
    }

    private fun SetVariables(imageUrl: String, ptitle_movie: String, padult: String , preleaseDate:String , pratingScore:String) {

        val image: ImageView = findViewById(R.id.U_movie_detail_image)
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(image)

        Log.d(ContentValues.TAG, "setImage: setting te image and name to widgets.")

        val title_movie = findViewById<TextView>(R.id.U_txt_Movie_Name)
        title_movie.text = ptitle_movie

        var adultval =" "
        if(padult.toBoolean()) adultval = "YES"
        else adultval = "NO"

        val adult = findViewById<TextView>(R.id.U_txt_adult)
        adult.text = adultval

        val releaseDate = findViewById<TextView>(R.id.U_txt_Like_ReleseaseDate)
        releaseDate.text = preleaseDate

        val ratingMovie = findViewById<TextView>(R.id.U_txt_Rating)
        var percentage = (pratingScore.toDouble() / 10) * 100

        var te = ("${percentage.toInt()} %")
        ratingMovie.text = te

    }

}