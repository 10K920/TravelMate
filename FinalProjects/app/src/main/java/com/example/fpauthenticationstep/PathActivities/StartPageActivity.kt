package com.example.fpauthenticationstep.PathActivities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fpauthenticationstep.AuthenticationActivities.LoginActivity
import com.example.fpauthenticationstep.AuthenticationActivities.RegisterActivity
import com.example.fpauthenticationstep.R
import kotlinx.android.synthetic.main.activity_main.*

// When the app starts, this page is shown
// User can either login or sign up for the app
class StartPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "TravelMate"

        to_register_btn.setOnClickListener {
            Log.d("StartPageActivity", "To register activity")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        to_logIn_btn.setOnClickListener {
            Log.d("StartPageActivity", "To login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}
