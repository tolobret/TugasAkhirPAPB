package com.example.tugasakhirpapb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Start : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val btn_signup = findViewById<Button>(R.id.btn_signup)

        btn_login.setOnClickListener {
            val go = Intent(this@Start, Login::class.java)
            startActivity(go)
        }

        btn_signup.setOnClickListener {
            val go = Intent(this@Start, Register::class.java)
            startActivity(go)
        }
    }
}