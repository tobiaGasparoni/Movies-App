package com.example.mr_kotlin.ui.upcoming_movies

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_kotlin.R
import com.example.mr_kotlin.data.MovieUpcoming.MovieUpcoming
import com.example.mr_kotlin.ui.trend_movie_detail.Movie_Detail_Trend_Activity
import com.example.mr_kotlin.ui.upcoming_movie_detail.upcoming_movie_detailActivity
import com.squareup.picasso.Picasso
import java.lang.reflect.Array.get

class UpcomingViewAdapter(): RecyclerView.Adapter<UpcomingViewAdapter.ViewHolder>() {

    var MoviesUpcoming: MutableList<MovieUpcoming> = ArrayList()
    lateinit var context: Context

    fun UpcomingViewAdapter(moviesUpcoming : MutableList<MovieUpcoming>, context: Context){
        this.MoviesUpcoming = moviesUpcoming
        this.context = context
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = MoviesUpcoming.get(position)
        holder.bind(item, context)

        holder.itemView.setOnClickListener(View.OnClickListener {

            Log.d(ContentValues.TAG, "onClick: clicked on: " + MoviesUpcoming.get(position))


            val intent = Intent(context, upcoming_movie_detailActivity::class.java)

            intent.putExtra("movie_image", MoviesUpcoming.get(position).posterComplete)
            intent.putExtra("title_movie", MoviesUpcoming.get(position).title)
            intent.putExtra("adult", MoviesUpcoming.get(position).adult.toString())
            //intent.putExtra("TotalLikes", mImageNames.get(position))
            //intent.putExtra("LikePercentage", mImageNames.get(position))
            intent.putExtra("releaseDate", MoviesUpcoming.get(position).releaseDate)
            intent.putExtra("ratingScore", MoviesUpcoming.get(position).ratingScore.toString())
            context.startActivity(intent)
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_row_upcoming, parent, false))
    }
    override fun getItemCount(): Int {
        return MoviesUpcoming.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val movie_image = view.findViewById(R.id.upcoming_movie_image) as ImageView

        val title_movie = view.findViewById(R.id.upcoming_title_movie) as TextView
        val releaseDate = view.findViewById(R.id.upcoming_releaseDate) as TextView

        fun bind(movieUpcoming: MovieUpcoming, context: Context){

            movie_image.loadUrl(movieUpcoming.posterComplete)

            title_movie.text = movieUpcoming.title
            releaseDate.text = movieUpcoming.releaseDate

            //itemView.setOnClickListener(View.OnClickListener { Toast.makeText(context, movieUpcoming.title, Toast.LENGTH_SHORT).show() })
        }
        fun ImageView.loadUrl(url: String) {
            Picasso.with(context).load(url).into(this)
        }
    }


}