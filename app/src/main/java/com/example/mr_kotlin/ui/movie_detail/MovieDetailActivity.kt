package com.example.mr_kotlin.ui.movie_detail

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mr_kotlin.R
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mr_kotlin.data.movie.Movie
import com.example.mr_kotlin.ui.login.LoginActivity
import com.example.mr_kotlin.utils.EventualConectivity.EventualConectivityManager
import com.example.mr_kotlin.utils.InjectorUtils
import com.example.mr_kotlin.utils.exceptions.UserNotLoggedInException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.random.Random

class MovieDetailActivity : AppCompatActivity() {

    private var index:Int = 0

    private var preLikeIndex:Int=-1

    private var userEmail = ""

    var movies: ArrayList<Movie> = ArrayList()



    // Get the HomeViewModelFactory with all of it's dependencies constructed
    private var factory: MovieDetailViewModelFactory? = null
    // Use ViewModelProviders class to create / get already created QuotesViewModel
    // for this view (activity)
    private var viewModel: MovieDetailViewModel? = null

    //For eventual connectivity
    //lateinit var eventualConectivityManager: EventualConectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideMovieDetailViewModelFactory()
        viewModel = ViewModelProvider(this, factory!!).get(MovieDetailViewModel::class.java)
        //eventualConectivityManager = EventualConectivityManager(this)
        Firebase.crashlytics.setUserId("user123456789")


        try{
            val currentUser: FirebaseUser? = viewModel!!.getCurrentAuthUser()
            setUserEmail()
            setContentView(R.layout.movie_detail)
            initializeUI(currentUser as FirebaseUser)

            //checkNetwork()

        } catch (e: UserNotLoggedInException) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    fun actualizarbtnNoInternet (Btn: ImageButton){
        Btn.visibility = View.GONE
        Btn.isClickable = false
    }

    fun actualizarbtnInternet (Btn: ImageButton){
        Btn.visibility = View.VISIBLE
        Btn.isClickable = true
    }

    fun getRamdonIds(): String {
        val ids = List(10) { Random.nextInt(0, 100) }
        var result = ""
        for (i in ids){
            result = if(result == ""){ i.toString()}
            else{
                "$result,$i"
            }

        }
            return result
    }

    private fun getRecommendation(){
        val currentActivity: AppCompatActivity = this
        lifecycleScope.launch {
            viewModel!!.getRecommendation(userEmail,getRamdonIds()).observe(currentActivity) {
                movies.addAll(it)
                loadImages()
            }
        }
    }

    private fun setUserEmail (){
        val currentUser: FirebaseUser? = viewModel!!.getCurrentAuthUser()
        if (currentUser != null) {
            userEmail = currentUser.email as String
        }
    }

    private  fun likeRecommendation(currentUser: FirebaseUser){
        if(movies.size == 0 || movies.size - 1 == index || preLikeIndex == index)return
        preLikeIndex = index
        val email = currentUser.email as String
        lifecycleScope.launch {
            viewModel!!.likeRecommendation(email, movies[index].getMovieId().toString())
        }
    }

    private fun initializeUI(currentUser: FirebaseUser) {
        //Eliminate the top name feature
        supportActionBar?.hide()
        setNavigation(currentUser)
        viewModel?.initializeSensorListener(this)
        this.getRecommendation()
    }

    private fun loadImages(){
        for(i in index until movies.size){
            Glide.with(this).load(movies[i].getPosterComplete()).preload()
        }
    }

    private fun setNavigation(currentUser: FirebaseUser) {

        Firebase.crashlytics.log("ERROR_MovieDatailView")

        val detailMovieButtonLike = findViewById<ImageButton>(R.id.detail_movie_ButtonLike)
        detailMovieButtonLike.setOnClickListener{
            likeRecommendation(currentUser)
            changeTheNewMovie()
        }

        val detailMovieButtonDislike = findViewById<ImageButton>(R.id.detail_movie_ButtonDislike)
        detailMovieButtonDislike.setOnClickListener{
            changeTheNewMovie()
        }

    }

    private fun changeTheNewMovie(){
        Log.d(TAG, "movies size" + movies.size + " index " + index)
        if(movies.size == 0 || movies.size - 1 == index) return
        Log.d(TAG, "paso movies size" + movies.size)
        val imageURL: String = movies[index].getPosterComplete()
        val overview:String = movies[index].getOverview()
        val ratingScore:String = movies[index].getRatingScore().toString()
        index += 1

        if(movies.size < index + 4){
            getRecommendation()
        }

        // Declaring and initializing the ImageView
        val detailMovieImg = findViewById<ImageView>(R.id.detail_movie_img)
        Glide.with(this).load(imageURL).into(detailMovieImg)

        val movieDetailTextOverview = findViewById<TextView>(R.id.movie_detail_textOverview) 
        movieDetailTextOverview.text = overview

        val movieDetailRatingScore = findViewById<TextView>(R.id.Movie_detail_ratingScore) 
        movieDetailRatingScore.text = ratingScore
    }

    override fun onResume() {
        viewModel!!.registerListener(this)
        super.onResume()
    }

    override fun onPause() {
        viewModel!!.unregisterListener(this)
        super.onPause()
    }
}
