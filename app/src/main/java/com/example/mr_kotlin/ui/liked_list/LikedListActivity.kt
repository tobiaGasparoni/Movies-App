package com.example.mr_kotlin.ui.liked_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_kotlin.R
import com.example.mr_kotlin.data.movie.MovieTrend
import com.example.mr_kotlin.data.movie.MoviedLiked.MovieLiked
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.ui.home.HomeViewModel
import com.example.mr_kotlin.ui.home.HomeViewModelFactory
import com.example.mr_kotlin.ui.movie_detail.MovieDetailActivity
import com.example.mr_kotlin.ui.profile.ProfileActivity
import com.example.mr_kotlin.ui.upcoming_movies.UpcomingActivity
import com.example.mr_kotlin.utils.EventualConectivity.EventualConectivityManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mr_kotlin.utils.InjectorUtils


class LikedListActivity : AppCompatActivity() {


    // Get the HomeViewModelFactory with all of it's dependencies constructed
    private var factory: LikedListViewModelFactory? = null
    // Use ViewModelProviders class to create / get already created QuotesViewModel
    // for this view (activity)
    private var viewModel: LikedListViewModel? = null


    lateinit var mRecyclerView : RecyclerView
    val mAdapter : LikedListAdapter = LikedListAdapter()

    //Eventual Connecctivity
    lateinit var eventualConectivityManager: EventualConectivityManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = InjectorUtils.provideLikedListViewModelFactory()
        viewModel = ViewModelProvider(this, factory!!).get(LikedListViewModel::class.java)


        setContentView(R.layout.liked_list)
        eventualConectivityManager = EventualConectivityManager(this)

        checkNetwork()
        initializeUI()

    }

    private fun checkNetwork() {

        eventualConectivityManager.observe(this,{ isNetworkAvailable ->
            //update ui
            val recycler_view_upcoming: RecyclerView = findViewById(R.id.recycler_view_liked)

            if(!isNetworkAvailable){

                Snackbar.make(findViewById(R.id.liked_list), "NO Internet Connection", Snackbar.LENGTH_INDEFINITE)
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

        setNavigation()
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
                R.id.mt_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.mt_Upcoming -> startActivity(Intent(this, UpcomingActivity::class.java))
            }
            true
        }

        val stuffBack = findViewById<ImageButton>(R.id.liked_ButtonBack)
        stuffBack.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }


    fun setUpRecyclerView() {
        // Create the observer which updates the UI.

        val nameObserver = Observer<ArrayList<MovieLiked>> { userName ->
            // Update the UI, in this case, a TextView.
            mRecyclerView = findViewById(R.id.recycler_view_liked) as RecyclerView
            mRecyclerView.setHasFixedSize(true)
            mRecyclerView.layoutManager = LinearLayoutManager(this)
            mAdapter.LikedListAdapter(userName,this)
            mRecyclerView.adapter = mAdapter
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel!!.movie_Liked.observe(this,nameObserver)

    }

}