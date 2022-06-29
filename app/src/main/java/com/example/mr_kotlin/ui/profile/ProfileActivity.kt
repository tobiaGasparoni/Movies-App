package com.example.mr_kotlin.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mr_kotlin.R
import com.anychart.AnyChartView
import com.anychart.AnyChart.pie
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.dataentry.DataEntry
import com.example.mr_kotlin.data.genre.GenrePieChart
import com.example.mr_kotlin.data.user.Picture
import com.example.mr_kotlin.data.user.User
import com.example.mr_kotlin.ui.edit_profile.EditProfileActivity
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.ui.liked_list.LikedListActivity
import com.example.mr_kotlin.ui.login.LoginActivity
import com.example.mr_kotlin.ui.movie_detail.MovieDetailActivity
import com.example.mr_kotlin.ui.upcoming_movies.UpcomingActivity
import com.example.mr_kotlin.utils.InjectorUtils
import com.example.mr_kotlin.utils.exceptions.UserNotLoggedInException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.Executors


class ProfileActivity : AppCompatActivity() {

    // Get the ProfileViewModelFactory with all of it's dependencies constructed
    private var factory: ProfileViewModelFactory? = null

    // Use ViewModelProviders class to create / get already created ProfileViewModel
    // for this view (activity)
    private var viewModel: ProfileViewModel? = null

    //list of the genres
    var genres: ArrayList<GenrePieChart> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        //Eliminate the top name feature
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideProfileViewModelFactory()
        viewModel = ViewModelProvider(this, factory!!).get(ProfileViewModel::class.java)

        try {
            val currentUser: FirebaseUser? = viewModel!!.getCurrentUser()
            checkLoggedIn(currentUser)
            setContentView(R.layout.profile)
            initializeUI(currentUser as FirebaseUser)
        } catch (e: UserNotLoggedInException) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkLoggedIn(currentUser: FirebaseUser?) {
        // Check if user is logged in.
        // If true, move to profile. Else, move to Login view.
        if(currentUser == null) {
            throw UserNotLoggedInException()
        }
    }

    private fun initializeUI(currentUser: FirebaseUser) {

        //Eliminate the top name feature
        supportActionBar?.hide()

        setNavigation()
        setUserNameText()
        setUserPicture()
        setGraph()

        viewModel?.initializeSensorListener(this)
    }

    private fun setNavigation() {
        /*******************************************************************************************
         * Listeners to allow the navigation through the app
         ******************************************************************************************/

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.mt_autoRenew -> startActivity(Intent(this,MovieDetailActivity::class.java))
                R.id.mt_home -> startActivity(Intent(this,HomeActivity::class.java))
                R.id.mt_Upcoming -> startActivity(Intent(this, UpcomingActivity::class.java))
            }
            true
        }

        val profileButtonBack = findViewById<ImageButton>(R.id.profile_ButtonBack)
        profileButtonBack.setOnClickListener{
            val Intent = Intent(this,HomeActivity::class.java)
            startActivity(Intent)
        }

        val profileEditProfilee = findViewById<Button>(R.id.profile_edit_profilee)
        profileEditProfilee.setOnClickListener{
            val Intent = Intent(this,EditProfileActivity::class.java)
            startActivity(Intent)
        }

        val ProfileLikedList = findViewById<Button>(R.id.Profile_liked_list)
        ProfileLikedList.setOnClickListener{
            val Intent = Intent(this, LikedListActivity::class.java)
            startActivity(Intent)
        }
    }

    private fun setGraph(){

        /***************************************************************************************************
         * Methods to implement de pie chart
         ************************************************************************************************/

        // Create the observer which updates the UI.
        val genresObserver = Observer<ArrayList<GenrePieChart>> {
            try {
                genres.addAll(it)

                val anyChartView = findViewById<AnyChartView>(R.id.profile_pieChart)
                val pie = pie()
                val dataEntry: MutableList<DataEntry> = ArrayList()
                for (i in genres.iterator()) {
                    val valorPorcentaje = i.getamountLikes()

                    if (valorPorcentaje > 0) {
                        val ne = ValueDataEntry(i.getgenreName(), valorPorcentaje)
                        dataEntry.add(ne)
                    }
                }
                pie.data(dataEntry)
                anyChartView.setChart(pie)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel!!.userStats.observe(this, genresObserver)

        /*
        var genresList: MutableMap<String,Double> = mutableMapOf("Action" to 5.0, "Drama" to 7.0) //= it.getGenresPieChart()
        var moviesArray:MutableSet<String> = genresList.keys
         var moviesArray: Array<String> = arrayOf("Comedy", "Drama", "Documentary","Crime","Adventure","Action")
        var percentage: Array<Int> = arrayOf(4,3,7,1,6,5)*/
    }

    private fun setUserNameText() {
        // Create the observer which updates the UI.
        val nameObserver = Observer<User> { user ->
            // Update the UI, in this case, a TextView.
            val txt = findViewById<TextView>(R.id.Profile_name)
            txt.text = user.getName().split(" ")[0]
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel!!.user.observe(this, nameObserver)
    }

    private fun setUserPicture() {

        // Create the observer which updates the UI.
        val picObserver = Observer<Picture> { user ->
            val imageurl = user.getimage()
            val detailMovieImg = findViewById<ImageView>(R.id.profile_image) as CircleImageView
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
        viewModel?.registerListener(this)
        super.onResume()
    }

    override fun onPause() {
        viewModel?.unregisterListener(this)
        super.onPause()
    }

}
