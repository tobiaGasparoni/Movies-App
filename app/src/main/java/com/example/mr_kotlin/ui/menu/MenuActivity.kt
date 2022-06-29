package com.example.mr_kotlin.ui.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mr_kotlin.R
import com.example.mr_kotlin.ui.usageStats.UsageStatsActivity
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.ui.liked_list.LikedListActivity
import com.example.mr_kotlin.ui.login.LoginActivity
import com.example.mr_kotlin.ui.movie_detail.MovieDetailActivity
import com.example.mr_kotlin.ui.signup.SignupActivity
import com.example.mr_kotlin.ui.upcoming_movies.UpcomingActivity
import com.example.mr_kotlin.utils.InjectorUtils
import com.example.mr_kotlin.utils.exceptions.UserNotLoggedInException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {

    // Get the LoginViewModelFactory with all of it's dependencies constructed
    private var factory: MenuViewModelFactory? = null
    // Use ViewModelProviders class to create / get already created QuotesViewModel
    // for this view (activity)
    private var viewModel: MenuViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideMenuViewModelFactory()
        viewModel = ViewModelProvider(this, factory!!).get(MenuViewModel::class.java)

        try {
            checkLoggedIn()
            setContentView(R.layout.menu)
            initializeUI()
        } catch (e: UserNotLoggedInException) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Method to check if the user is login
     */
    private fun checkLoggedIn() {
        // Check if user is logged in.
        // If true, move to home. Else, stay in Login view.
        if(viewModel!!.getCurrentAuthUser() == null) {
            throw UserNotLoggedInException()
        }
    }

    private fun initializeUI() {
        //Eliminate the top name feature
        supportActionBar?.hide()

        /***************************************************************************************************
         * Methods to implement de navigability through the app
         **************************************************************************************************/


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.mt_autoRenew -> startActivity(Intent(this,MovieDetailActivity::class.java))
                R.id.mt_home -> startActivity(Intent(this,HomeActivity::class.java))
                R.id.mt_Upcoming -> startActivity(Intent(this, UpcomingActivity::class.java))
            }
            true
        }

        val stuffBack = findViewById<ImageButton>(R.id.Stuff_Back)
        stuffBack.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val logout = findViewById<ImageButton>(R.id.logout)
        logout.setOnClickListener{
            viewModel!!.logout()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val likedList = findViewById<ImageButton>(R.id.Stuff_List_imageButton6)
        likedList.setOnClickListener{
            val intent = Intent(this, LikedListActivity::class.java)
            startActivity(intent)
        }

        val stuffUsageStats = findViewById<Button>(R.id.stuff_usageStats)
        stuffUsageStats.setOnClickListener{
            val intent = Intent(this, UsageStatsActivity::class.java)
            startActivity(intent)
        }

        val deleteAccount = findViewById<ImageButton>(R.id.delete_account_icon)
        deleteAccount.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to delete your account?")
                .setCancelable(true)
                .setPositiveButton("Yes") { dialog, id ->
                    lifecycleScope.launch() {
                        viewModel!!.deleteAccount()
                    }
                    viewModel!!.logout()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }
}