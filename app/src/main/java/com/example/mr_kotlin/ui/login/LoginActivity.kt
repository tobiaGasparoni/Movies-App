package com.example.mr_kotlin.ui.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mr_kotlin.R
import com.example.mr_kotlin.ui.signup.SignupActivity
import com.example.mr_kotlin.ui.home.HomeActivity
import com.example.mr_kotlin.utils.EventualConectivity.EventualConectivityManager
import com.example.mr_kotlin.utils.InjectorUtils
import com.example.mr_kotlin.utils.exceptions.UserLoggedInException
import kotlinx.coroutines.launch
import android.graphics.Color
import com.google.android.material.snackbar.Snackbar
import android.provider.Settings
import com.example.mr_kotlin.utils.Utils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase


/***************************************************************************************************
 *
 **************************************************************************************************/
class LoginActivity : AppCompatActivity() {

    // Get the LoginViewModelFactory with all of it's dependencies constructed
    private var factory: LoginViewModelFactory? = null
    // Use ViewModelProviders class to create / get already created QuotesViewModel
    // for this view (activity)
    private var viewModel: LoginViewModel? = null

    lateinit var eventualConectivityManager: EventualConectivityManager


    /***********************************************************************************************
     *
     **********************************************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = InjectorUtils.provideLoginViewModelFactory()
        viewModel = ViewModelProvider(this, factory!!).get(LoginViewModel::class.java)
        eventualConectivityManager = EventualConectivityManager(this)
        Firebase.crashlytics.setUserId("user123456789")

        /*
        val sharedPreferences = getPreferences(MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putString("email", viewModel?.getCurrentAuthUser()?.email as String)
        editor.commit()*/


        try {
            checkLoggedIn()
            checkNetwork()
            setContentView(R.layout.login)
            initializeUI()
        } catch (e: UserLoggedInException) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkNetwork() {

        eventualConectivityManager.observe(this,{ isNetworkAvailable ->
            //update ui

            val btnLogin: Button = findViewById(R.id.buttonLogin)
            val btnsignup: Button = findViewById(R.id.buttonMoveToSignup)

            if(!isNetworkAvailable){

                Snackbar.make(findViewById(R.id.login_layout), "NO Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Open Settings") {
                        // Responds to click on the action
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                    }
                    .show()
                
                actualizarbtn(btnLogin,false,"#808080")
                actualizarbtn(btnsignup,false,"#808080")
            }
            else{

                Snackbar.make(findViewById(R.id.login_layout), "Internet Connection Available", Snackbar.LENGTH_SHORT)
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
        Firebase.crashlytics.log("ERROR_LoginView")

        setupButtons()
    }

    private fun setupButtons() {




        // Codigo para probar fallos en la app NO quitar
        /*val btnfallo: Button = findViewById(R.id.button_fallo)
        btnfallo.text = "Test Crash"
        btnfallo.setOnClickListener {
            Firebase.crashlytics.log("ERROR_HomeView")
            throw RuntimeException("Test Crash")
        }*/



        // Assigns a listener to the Login button to log in the use with the written credentials
        // so that the user can start using all of the app's features.
        val btnLogin:Button = findViewById(R.id.buttonLogin)

        btnLogin.setOnClickListener {
            loginButton()
        }

        // Assigns a listener to the Signup button to change to the Signup activity
        // so that the user can register to the app if it doesn't have an account.
        val btnSignup: Button = findViewById(R.id.buttonMoveToSignup)
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginButton() {
        val currentActivity: AppCompatActivity = this
        lifecycleScope.launch {
            // Capture Strings in inputs to create a User object
            val email: String = (findViewById<TextInputEditText>(R.id.inputEmail_edit)).text.toString()
            val password: String = (findViewById<TextInputEditText>(R.id.inputPasswordLogin_edit)).text.toString()

            // Creates new account in the authentication server
            try {
                viewModel!!.login(email, password)
                val intent = Intent(currentActivity, HomeActivity::class.java)
                currentActivity.startActivity(intent)
            } catch (e: Exception) {
                Utils.showSnackbar(currentActivity, e.message ?: "Login failed." )
            }
        }
    }
}