package com.example.ecolab


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 1000)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        val nextActivity = if (auth.currentUser != null) {
            HomeActivity::class.java
        } else {
            Login::class.java
        }

        val intent = Intent(this, nextActivity)
        startActivity(intent)
        finish()
    }
}
