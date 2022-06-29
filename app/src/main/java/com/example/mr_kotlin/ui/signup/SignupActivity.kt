package com.example.mr_kotlin.ui.signup

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mr_kotlin.R
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.utils.EventualConectivity.EventualConectivityManager
import com.example.mr_kotlin.utils.InjectorUtils
import com.example.mr_kotlin.utils.exceptions.UserLoggedInException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.launch

/***************************************************************************************************
 *
 **************************************************************************************************/
class SignupActivity : AppCompatActivity() {

    // Get the LoginViewModelFactory with all of it's dependencies constructed
    private var factory: SignupViewModelFactory? = null
    // Use ViewModelProviders class to create / get already created QuotesViewModel
    // for this view (activity)
    private var viewModel: SignupViewModel? = null

    lateinit var eventualConectivityManager: EventualConectivityManager

    /***********************************************************************************************
     *
     **********************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideSignupViewModelFactory()
        viewModel = ViewModelProvider(this, factory!!).get(SignupViewModel::class.java)
        eventualConectivityManager = EventualConectivityManager(this)

        try {
            checkLoggedIn()
            checkNetwork()
            setContentView(R.layout.signup)
            initializeUI()
        } catch (e: UserLoggedInException) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkNetwork() {

        eventualConectivityManager.observe(this,{ isNetworkAvailable ->
            //update ui

            val btnsignup: Button = findViewById(R.id.signup)

            if(!isNetworkAvailable){

                Snackbar.make(findViewById(R.id.signup_layout), "NO Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Open Settings") {
                        // Responds to click on the action
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                    }
                    .show()


                actualizarbtn(btnsignup,false,"#808080")

            }
            else{

                Snackbar.make(findViewById(R.id.signup_layout), "Internet Connection Available", Snackbar.LENGTH_SHORT)
                    .setAction("  ") {
                    }
                    .show()

                actualizarbtn(btnsignup,true,"#C1121F")

            }
        })
    }

    fun actualizarbtn (Btn: Button,bool: Boolean, color: String ){
        Btn.isEnabled = bool
        Btn.isClickable = bool
        Btn.setBackgroundColor(Color.parseColor(color))

    }

    private fun checkLoggedIn() {
        // Check if user is logged in.
        // If true, move to home. Else, stay in Login view.
        if(viewModel!!.getCurrentAuthUser() != null) {
            throw UserLoggedInException()
        }
    }

    private fun initializeUI() {

        //Eliminate the top name feature
        supportActionBar?.hide()

        setupButtons()
    }

    private fun setupButtons() {
        val btnLogin: Button = findViewById(R.id.signup)
        btnLogin.setOnClickListener {
            signupButton()
        }

        val bdayEditText:TextInputEditText = findViewById<TextInputEditText>(R.id.inputBirthday_edit)

        bdayEditText.setOnClickListener { showDatePickerDialog() }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(fragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val bdayEditText: TextInputEditText = findViewById<TextInputEditText>(R.id.inputBirthday_edit)
        val text = "$month/$day/$year"
        bdayEditText.setText(text)
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    private fun signupButton() {
        // Capture Strings in inputs to create a User object
        val name: String = (findViewById<TextInputEditText>(R.id.inputName_edit)).text.toString()
        val email: String = (findViewById<TextInputEditText>(R.id.inputEmail_edit)).text.toString()
        val password: String = (findViewById<TextInputEditText>(R.id.inputPassword_edit)).text.toString()
        val repeatPassword: String = (findViewById<TextInputEditText>(R.id.inputRepeatPassword_edit))
            .text.toString()
        val birthday: String = (findViewById<TextInputEditText>(R.id.inputBirthday_edit)).text.toString()

        val arr = birthday.split("/")
        val date: String = arr[2] + "-" + arr[0] + "-" + arr[1] + " 00:00:00.000"

        if(password != repeatPassword) {
            Toast.makeText(this,"The two passwords must be equal!", Toast.LENGTH_SHORT).show()
            return
        }

        val currentActivity: AppCompatActivity = this
        lifecycleScope.launch {
            // Creates new account in the authentication server
            try {
                viewModel!!.createNewAuthUser(currentActivity, name, email, password, date)
                viewModel!!.createNewDBUser(name, email, birthday)
                val intent = Intent(currentActivity, HomeActivity::class.java)
                startActivity(intent)
            } catch (e: FirebaseAuthUserCollisionException) {
                Toast.makeText(currentActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}