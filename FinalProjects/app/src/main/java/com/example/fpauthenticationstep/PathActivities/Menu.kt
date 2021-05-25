package com.example.fpauthenticationstep.PathActivities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.fpauthenticationstep.MapActivities.MapsActivity
import com.example.fpauthenticationstep.MessagingActivities.CurrentMessages
import com.example.fpauthenticationstep.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*

// When a user logIn to the app, this page shows up
// user can decide whether to search a place to explore or read messages 
class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        supportActionBar?.title = "Menu"

        toMap_btn.setOnClickListener {
           // val intent = Intent(this, SearchPlaces::class.java)
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        toCurM_btn.setOnClickListener {
            val intent = Intent(this, CurrentMessages::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, StartPageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
