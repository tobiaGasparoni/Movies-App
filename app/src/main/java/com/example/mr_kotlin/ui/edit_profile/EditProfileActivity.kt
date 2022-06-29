package com.example.mr_kotlin.ui.edit_profile

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.mr_kotlin.R
import com.example.mr_kotlin.data.user.Picture
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.ui.movie_detail.MovieDetailActivity
import com.example.mr_kotlin.ui.profile.ProfileActivity
import com.example.mr_kotlin.ui.signup.DatePickerFragment
import com.example.mr_kotlin.ui.upcoming_movies.UpcomingActivity
import com.example.mr_kotlin.utils.EventualConectivity.EventualConectivityManager
import com.example.mr_kotlin.utils.InjectorUtils
import com.example.mr_kotlin.utils.exceptions.UserNotLoggedInException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class EditProfileActivity : AppCompatActivity() {

    // Get the HomeViewModelFactory with all of it's dependencies constructed
    private var factory: EditProfileViewModelFactory? = null
    // Use ViewModelProviders class to create / get already created QuotesViewModel
    // for this view (activity)
    private var viewModel: EditProfileViewModel? = null

    //Eventual Connecctivity
    lateinit var eventualConectivityManager: EventualConectivityManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = InjectorUtils.provideEditProfileViewModelFactory()
        viewModel = ViewModelProvider(this, factory!!).get(EditProfileViewModel::class.java)

        eventualConectivityManager = EventualConectivityManager(this)

        try {
            val currentUser: FirebaseUser? = viewModel!!.getCurrentAuthUser()
            checkLoggedIn(currentUser)
            setContentView(R.layout.activity_edit_profile_stats)
            checkNetwork()
            Log.d(TAG, "ENTRO oncreate:")
            initializeUI(currentUser as FirebaseUser)

        } catch (e: UserNotLoggedInException) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkNetwork() {

        eventualConectivityManager.observe(this,{ isNetworkAvailable ->
            //update ui
            val btnLogin: Button = findViewById(R.id.edit_button_change_picture)
            val btnsignup: Button = findViewById(R.id.save_buttonn)

            if(!isNetworkAvailable){

                Snackbar.make(findViewById(R.id.editlayout), "NO Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Open Settings") {
                        // Responds to click on the action
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                    }
                    .show()

                actualizarbtn(btnLogin,false,"#808080")
                actualizarbtn(btnsignup,false,"#808080")
            }
            else{

                Snackbar.make(findViewById(R.id.editlayout), "Internet Connection Available", Snackbar.LENGTH_SHORT)
                    .setAction("  ") {
                    }
                    .show()

                actualizarbtn(btnLogin,true,"#C1121F")
                actualizarbtn(btnsignup,true,"#C1121F")

            }
        })
    }

    fun actualizarbtn (Btn: Button,bool: Boolean, color: String ){
        Btn.isEnabled = bool
        Btn.isClickable = bool
        Btn.setBackgroundColor(Color.parseColor(color))

    }

    private fun checkLoggedIn(currentUser: FirebaseUser?) {
        // Check if user is logged in.
        // If true, move to home. Else, move to Login view.
        if(currentUser == null) {
            throw UserNotLoggedInException()
        }
    }

    private fun initializeUI(currentUser: FirebaseUser) {

        Log.d(TAG, "ENTRO ini:")
        //Eliminate the top name feature
        supportActionBar?.hide()

        setNavigation()
        setUserPicture(currentUser)
        setupButtons(currentUser)

    }

    private fun setNavigation() {
        /*******************************************************************************************
         * Listeners to allow the navigation through the app
         ******************************************************************************************/


        val backbutton = findViewById<ImageButton>(R.id.editProfile_ButtonBack)
        backbutton.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val save = findViewById<Button>(R.id.save_buttonn)
        save.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setUserPicture(currentUser: FirebaseUser) {

        // Create the observer which updates the UI.
        val picObserver = Observer<Picture> { user ->
            val imageurl = user.getimage()
            val detailMovieImg = findViewById<ImageView>(R.id.edit_profilePic) as CircleImageView
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

    private fun setupButtons(currentUser: FirebaseUser) {

        val btnLogin: Button = findViewById(R.id.save_buttonn)
        btnLogin.setOnClickListener {

            signupButton(currentUser)
        }

        Log.d(TAG, "ENTRO setup:")

        val bdayEditText: TextInputEditText = findViewById<TextInputEditText>(R.id.edit_Birthday_editt)

        bdayEditText.setOnClickListener { showDatePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(fragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val bdayEditText: TextInputEditText = findViewById<TextInputEditText>(R.id.edit_Birthday_editt)
        val text = "$month/$day/$year"
        bdayEditText.setText(text)
    }


    private fun signupButton(currentUser: FirebaseUser) {
        // Capture Strings in inputs to create a User object

        Log.d(ContentValues.TAG, "ENTRO SIGN:")

        val imagePicture: String = "https://i.pinimg.com/474x/9e/5e/49/9e5e490d40e102e5077617ff3c07a24d.jpg"

        val name: String = (findViewById<TextInputEditText>(R.id.edit_name_editt)).text.toString()
        val birthday: String = (findViewById<TextInputEditText>(R.id.edit_Birthday_editt)).text.toString()
        val arr = birthday.split("/")
        val date: String = arr[2] + "-" + arr[0] + "-" + arr[1] + " 00:00:00.000"


        Log.d(ContentValues.TAG, "VALOR: $imagePicture")
        Log.d(ContentValues.TAG, "VALOR 2: $name")
        Log.d(ContentValues.TAG, "VALOR 3: $date")
        Log.d(ContentValues.TAG, "VALOR 4: ${currentUser.email}")


        val currentActivity: AppCompatActivity = this
        lifecycleScope.launch {
            // Creates new account in the authentication server
            currentUser.email?.let { viewModel!!.updateUser(imagePicture,name,it,date) }

            val intent = Intent(currentActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }







}