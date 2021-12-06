package com.example.tugasakhirpapb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        lateinit var mAuth : FirebaseAuth
        lateinit var currentUser : FirebaseUser

        val user = mAuth.currentUser
    }
}