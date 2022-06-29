package com.example.mr_kotlin.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_kotlin.R
import com.example.mr_kotlin.data.movie.MovieTrend
import com.example.mr_kotlin.data.user.Picture
import com.example.mr_kotlin.data.user.User
import com.example.mr_kotlin.ui.login.LoginActivity
import com.example.mr_kotlin.ui.menu.MenuActivity
import com.example.mr_kotlin.ui.movie_detail.MovieDetailActivity
import com.example.mr_kotlin.ui.profile.ProfileActivity
import com.example.mr_kotlin.ui.upcoming_movies.UpcomingActivity
import com.example.mr_kotlin.utils.EventualConectivity.EventualConectivityManager
import com.example.mr_kotlin.utils.InjectorUtils
import com.example.mr_kotlin.utils.exceptions.UserNotLoggedInException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.Executors


class HomeActivity : AppCompatActivity() {

    // Get the HomeViewModelFactory with all of it's dependencies constructed
    private var factory: HomeViewModelFactory? = null
    // Use ViewModelProviders class to create / get already created QuotesViewModel
    // for this view (activity)
    private var viewModel: HomeViewModel? = null

    //Eventual Connecctivity
    lateinit var eventualConectivityManager: EventualConectivityManager

    lateinit var mRecyclerView : RecyclerView
    val mAdapter : TrendViewAdapter = TrendViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = InjectorUtils.provideHomeViewModelFactory()
        viewModel = ViewModelProvider(this, factory!!).get(HomeViewModel::class.java)

        eventualConectivityManager = EventualConectivityManager(this)
        Firebase.crashlytics.setUserId("user123456789")

        try {
            val currentUser: FirebaseUser? = viewModel!!.getCurrentAuthUser()
            checkLoggedIn(currentUser)
            setContentView(R.layout.home)
            //checkNetwork()
            initializeUI(currentUser as FirebaseUser)
        } catch (e: UserNotLoggedInException) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkNetwork() {

        eventualConectivityManager.observe(this,{ isNetworkAvailable ->
            //update ui
            val recycler_view_upcoming: RecyclerView = findViewById(R.id.recycler_view_trend)

            if(!isNetworkAvailable){

                Snackbar.make(findViewById(R.id.home_first), "NO Internet Connection", Snackbar.LENGTH_INDEFINITE)
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

    private fun checkLoggedIn(currentUser: FirebaseUser?) {
        // Check if user is logged in.
        // If true, move to home. Else, move to Login view.
        if(currentUser == null) {
            throw UserNotLoggedInException()
        }
    }

    private fun initializeUI(currentUser: FirebaseUser) {

        //Eliminate the top name feature
        supportActionBar?.hide()

        setUpRecyclerView()
        setNavigation()
        setUserNameText()
        setUserPicture(currentUser)


        viewModel!!.initializeSensorListener(this)
    }

    private fun setNavigation() {
        /*******************************************************************************************
         * Listeners to allow the navigation through the app
         ******************************************************************************************/

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.mt_autoRenew -> startActivity(Intent(this,MovieDetailActivity::class.java))
                R.id.mt_Upcoming -> startActivity(Intent(this,UpcomingActivity::class.java))
            }
            true
        }

        val homeBurgerBottom = findViewById<ImageButton>(R.id.home_burger_buttom)
        homeBurgerBottom.setOnClickListener{
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }

        val homeProfilePic = findViewById<CircleImageView>(R.id.home_profilePic)
        homeProfilePic.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }
    }

     fun setUpRecyclerView() {
        // Create the observer which updates the UI.
        val nameObserver = Observer<ArrayList<MovieTrend>> { userName ->
            // Update the UI, in this case, a TextView.
            mRecyclerView = findViewById(R.id.recycler_view_trend) as RecyclerView
            mRecyclerView.setHasFixedSize(true)
            mRecyclerView.layoutManager = LinearLayoutManager(this)
            mAdapter.TrendViewAdapter(userName,this)
            mRecyclerView.adapter = mAdapter
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel!!.MovieTrend.observe(this,nameObserver)

    }

    private fun setUserNameText() {
        // Create the observer which updates the UI.
        val nameObserver = Observer<User> { userName ->
            // Update the UI, in this case, a TextView.
            val txt = findViewById<TextView>(R.id.home_name)
            txt.text = userName.getName().split(" ")[0]
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel!!.user.observe(this, nameObserver)
    }

    private fun setUserPicture(currentUser: FirebaseUser) {

        // Create the observer which updates the UI.
        val picObserver = Observer<Picture> { user ->
            val imageurl = user.getimage()
            val detailMovieImg = findViewById<ImageView>(R.id.home_profilePic) as CircleImageView
            uploadImage(detailMovieImg, imageurl)
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel!!.userPic.observe(this, picObserver)
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

    override fun onResume() {
        viewModel!!.registerListener(this)
        super.onResume()
    }

    override fun onPause() {
        viewModel!!.unregisterListener(this)
        super.onPause()
    }



}