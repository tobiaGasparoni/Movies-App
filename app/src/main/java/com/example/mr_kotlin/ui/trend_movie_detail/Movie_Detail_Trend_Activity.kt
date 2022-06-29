package com.example.mr_kotlin.ui.trend_movie_detail

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mr_kotlin.R
import com.bumptech.glide.Glide
import android.graphics.Bitmap
import android.widget.ImageView

import android.widget.TextView




class Movie_Detail_Trend_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail_trend)

        getIncomingIntent()
    }


    private fun getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.")

        if (intent.hasExtra("movie_image")
            && intent.hasExtra("title_movie")
            && intent.hasExtra("time")
            && intent.hasExtra("ratingMovie")
            && intent.hasExtra("numLikes")) {

            Log.d(TAG, "getIncomingIntent: found intent extras.")

            val movie_image:String = intent.getStringExtra("movie_image").toString()
            val title_movie:String = intent.getStringExtra("title_movie").toString()
            val time = intent.getStringExtra("time").toString()
            val numLikes = intent.getStringExtra("numLikes").toString()
            val ratingMovie = intent.getStringExtra("ratingMovie").toString()

            SetVariables(movie_image, title_movie,time ,numLikes,ratingMovie )
        }
    }

    private fun SetVariables(imageUrl: String, ptitle_movie: String, ptime: String, pnumLikes: String , pratingMovie:String) {

        val image: ImageView = findViewById(R.id.T_movie_detail_image)
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(image)

        Log.d(TAG, "setImage: setting te image and name to widgets.")


        val title_movie = findViewById<TextView>(R.id.T_txt_Movie_Name)
        title_movie.text = ptitle_movie

        val time = findViewById<TextView>(R.id.T_txtTime)
        time.text = "$ptime min"


        val numLikes = findViewById<TextView>(R.id.T_txt_numlikes)
        numLikes.text = pnumLikes

        val ratingMovie = findViewById<TextView>(R.id.T_txt_Rating)
        var percentage = (pratingMovie.toDouble() / 10) * 100
        Log.d(TAG, "Set variables: 5: " + percentage)
        var te = ("${percentage.toInt()} %")
        ratingMovie.text = te


    }

}