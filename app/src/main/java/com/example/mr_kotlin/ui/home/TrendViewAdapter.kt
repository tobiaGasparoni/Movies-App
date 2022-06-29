package com.example.mr_kotlin.ui.home


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mr_kotlin.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_kotlin.data.movie.MovieTrend
import com.example.mr_kotlin.ui.movie_detail.MovieDetailActivity
import com.example.mr_kotlin.ui.trend_movie_detail.Movie_Detail_Trend_Activity
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso


class TrendViewAdapter(): RecyclerView.Adapter<TrendViewAdapter.ViewHolder>(){


    var MoviesTrend: MutableList<MovieTrend> = ArrayList()
    lateinit var context: Context

    fun TrendViewAdapter(movies : MutableList<MovieTrend>, context: Context){
        this.MoviesTrend = movies
        this.context = context
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = MoviesTrend.get(position)
        holder.bind(item, context)


        holder.itemView.setOnClickListener(View.OnClickListener {

            Log.d(TAG, "onClick: clicked on: " + MoviesTrend.get(position))


            val intent = Intent(context, Movie_Detail_Trend_Activity::class.java)
            intent.putExtra("movie_image", MoviesTrend.get(position).posterComplete)
            intent.putExtra("title_movie", MoviesTrend.get(position).title)
            intent.putExtra("time", MoviesTrend.get(position).runTime.toString())
            intent.putExtra("numLikes", MoviesTrend.get(position).numLikes.toString())
            intent.putExtra("ratingMovie", MoviesTrend.get(position).ratingScore.toString())
            context.startActivity(intent)
        })


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_row_trend, parent, false))
    }
    override fun getItemCount(): Int {
        return MoviesTrend.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val movie_image = view.findViewById(R.id.trend_movie_image) as ShapeableImageView
        val title_movie = view.findViewById(R.id.trend_title_movie) as TextView
        val time = view.findViewById(R.id.trend_time_movie) as TextView
        val ratingMovie = view.findViewById(R.id.trend_rating) as TextView

        fun bind(movieTrend: MovieTrend, context: Context){

            movie_image.loadUrl(movieTrend.posterComplete)
            title_movie.text = movieTrend.title

            var timeMovie: CharSequence = ("${movieTrend.runTime} min")
            time.setText(timeMovie)

            var rating: CharSequence = ("${movieTrend.ratingScore}/10")
            ratingMovie.text = rating

        }
        fun ShapeableImageView.loadUrl(url: String) {
            Picasso.with(context).load(url).into(this)
        }
    }




}