package com.example.mr_kotlin.ui.liked_list

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_kotlin.R
import com.example.mr_kotlin.data.movie.MoviedLiked.MovieLiked
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.Executors

class LikedListAdapter(): RecyclerView.Adapter<LikedListAdapter.ViewHolder>() {


    var likedMovies: MutableList<MovieLiked> = ArrayList()
    lateinit var context: Context

    fun LikedListAdapter(likedMovies : MutableList<MovieLiked>, context: Context ){
        this.likedMovies = likedMovies
        this.context = context
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = likedMovies.get(position)
        holder.bind(item, context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_row_liked_movies, parent, false))
    }
    override fun getItemCount(): Int {
        return likedMovies.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val liked_movie_image = view.findViewById(R.id.liked_movie_image) as ImageView

        val liked_title_movie = view.findViewById(R.id.liked_title_movie) as TextView
        val liked_releaseDate = view.findViewById(R.id.liked_releaseDate) as TextView
        val liked_time_movie = view.findViewById(R.id.liked_time_movie) as TextView
        val liked_adult = view.findViewById(R.id.liked_adult) as TextView
        val liked_rating = view.findViewById(R.id.liked_rating) as TextView

        fun bind(movieLiked: MovieLiked, context: Context){

            liked_movie_image.loadUrl(movieLiked.posterComplete)

            print("this---------  $movieLiked.posterComplete")

            //Picasso.with(context).load(movieLiked.posterComplete).into(liked_movie_image)

            liked_title_movie.text = movieLiked.title
            liked_releaseDate.text = movieLiked.releaseDate

            var timeMovie: CharSequence = ("${movieLiked.runTime} min")
            liked_time_movie.setText(timeMovie)

            var adult: CharSequence
            if (movieLiked.adult){
                adult = "Adult Content"}
            else{
                adult = "NOT Adult Content"}
            liked_adult.text = adult

            var rating: CharSequence = ("${movieLiked.ratingScore}/10")
            liked_rating.text = rating

            itemView.setOnClickListener(View.OnClickListener { Toast.makeText(context, movieLiked.title, Toast.LENGTH_SHORT).show() })
        }
        fun ImageView.loadUrl(url: String) {
            Picasso.with(context).load(url).into(this)
        }
    }



    private fun uploadImage(imageview: CircleImageView, url:String){
        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()
        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())
        // Initializing the image
        var image: Bitmap?

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {
            // Image URL

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(url).openStream()
                image = BitmapFactory.decodeStream(`in`)
                // Only for making changes in UI
                handler.post {
                    imageview.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}