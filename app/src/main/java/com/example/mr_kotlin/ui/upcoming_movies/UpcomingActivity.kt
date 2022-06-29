package com.example.mr_kotlin.ui.upcoming_movies

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_kotlin.R
import com.example.mr_kotlin.data.MovieUpcoming.MovieUpcoming
import com.example.mr_kotlin.data.MovieUpcoming.RetrofitService
import com.example.mr_kotlin.data.genre.GenrePieChart
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.ui.menu.MenuActivity
import com.example.mr_kotlin.ui.movie_detail.MovieDetailActivity
import com.example.mr_kotlin.utils.EventualConectivity.EventualConectivityManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit

class UpcomingActivity : AppCompatActivity() {


    //Recycler view methods
    var MoviesUpcoming = ArrayList<MovieUpcoming>()

    lateinit var mRecyclerView : RecyclerView
    val mAdapter : UpcomingViewAdapter = UpcomingViewAdapter()


    //Eventual Connecctivity
    lateinit var eventualConectivityManager: EventualConectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming)

        eventualConectivityManager = EventualConectivityManager(this)

        checkNetwork()
        initializeUI()


    }

    private fun checkNetwork() {

        eventualConectivityManager.observe(this,{ isNetworkAvailable ->
            //update ui
            val recycler_view_upcoming: RecyclerView = findViewById(R.id.recycler_view_upcoming)

            if(!isNetworkAvailable){

                Snackbar.make(findViewById(R.id.upcoming_layout), "NO Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Open Settings") {
                        // Responds to click on the action
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                    }
                    .show()

                recycler_view_upcoming.visibility = View.GONE
                recycler_view_upcoming.isClickable = false
            }
            else{
                recycler_view_upcoming.visibility = View.VISIBLE
                recycler_view_upcoming.isClickable = true

            }
        })
    }



    private fun initializeUI() {

        //Eliminate the top name feature
        supportActionBar?.hide()

        chargeUpcomingMovies()
        setNavigation()
    }

    private fun chargeUpcomingMovies() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .build()

        // Create Service
        val service = retrofit.create(RetrofitService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            /*
             * For @Query: You need to replace the following line with val response = service.getEmployees(2)
             * For @Path: You need to replace the following line with val response = service.getEmployee(53)
             */

            // Do the GET request and get response
            val response = service.getUpcoming()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )
                    createListOfUpcomingMovies(prettyJson)
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    private fun createListOfUpcomingMovies(jsonString: String){

        val answer = JSONObject(jsonString)
        val results: JSONArray = answer.getJSONArray("results")

        for ( i in 0 until results.length()){

            val moJso = results.getJSONObject(i)
            //Log.d("INICIAL", moJso.toString())
            var posterComplete:String = moJso["poster_path"].toString()
            var urlImge = "https://www.themoviedb.org/t/p/w1280$posterComplete"
            val movie = MovieUpcoming(
                urlImge,
                moJso["title"].toString(),
                moJso["adult"].toString().toBoolean(),
                moJso["release_date"].toString(),
                moJso["vote_average"].toString().toDouble()
            )
            MoviesUpcoming.add(movie)
        }

        setUpRecyclerView()
    }

    private fun setNavigation() {
        /*******************************************************************************************
         * Listeners to allow the navigation through the app
         ******************************************************************************************/

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.mt_autoRenew -> startActivity(Intent(this, MovieDetailActivity::class.java))
                R.id.mt_home -> startActivity(Intent(this,HomeActivity::class.java))
                R.id.mt_Upcoming -> startActivity(Intent(this,UpcomingActivity::class.java))
            }
            true
        }

        val homeBurgerBottom = findViewById<ImageButton>(R.id.upcoming_burger_buttom)
        homeBurgerBottom.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    fun setUpRecyclerView(){
        mRecyclerView = findViewById(R.id.recycler_view_upcoming) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.UpcomingViewAdapter(MoviesUpcoming,this)
        mRecyclerView.adapter = mAdapter
    }

}