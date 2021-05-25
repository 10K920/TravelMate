package com.example.fpauthenticationstep.AuthenticationActivities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fpauthenticationstep.PathActivities.Menu
import com.example.fpauthenticationstep.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

// Users can signIn with email address and pw registerd in the Firebase Authentication
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        logIn_fin_btn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = emailIn.text.toString()
        val password = pwIn.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this@LoginActivity, "Wrong ID or PW", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "Successfully signed in with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("Main", "Failed to sign in user: ${it.message}")
                Toast.makeText(this, "Failed to sign in user: ${it.message}", Toast.LENGTH_SHORT).show()
            }

        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }
}
