package com.example.tugasakhirpapb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tugasakhirpapb.fragment.CreateFragment
import com.example.tugasakhirpapb.fragment.HomeFragment
import com.example.tugasakhirpapb.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navbar)

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val createPostFragment = CreateFragment()

        var bottomNav : BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment -> replaceFragment(homeFragment)
                R.id.profileFragment -> replaceFragment(profileFragment)
                R.id.createFragment -> replaceFragment(createPostFragment)
            }
            true
        }

        replaceFragment(homeFragment)
    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }
}