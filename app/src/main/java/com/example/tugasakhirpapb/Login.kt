package com.example.tugasakhirpapb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lateinit var mAuth : FirebaseAuth
        lateinit var currentUser : FirebaseUser

        val loginBtn : Button = findViewById(R.id.login_btn)
        val email : EditText = findViewById(R.id.login_email)
        val pass : EditText = findViewById(R.id.login_pass)
        val signUpBtn : Button = findViewById(R.id.signUpBtn)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        loginBtn.setOnClickListener{
            val inputEmail = email.text.toString()
            val inputPass = pass.text.toString()
            if(inputEmail.isEmpty()){
                Toast.makeText(baseContext, "Silahkan mengisi kolom email", Toast.LENGTH_SHORT).show()
            }else if (inputPass.isEmpty()){
                Toast.makeText(baseContext, "Silahkan mengisi kolom password", Toast.LENGTH_SHORT).show()
            }else if (inputEmail.isEmpty() && inputPass.isEmpty()){
                Toast.makeText(baseContext, "Password dan Email harus diisi", Toast.LENGTH_SHORT).show()
            }

            mAuth.signInWithEmailAndPassword(inputEmail, inputPass)
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        val intent2 = Intent (this, RecyclerViewActivity::class.java)
                        startActivity(intent2)
                    }else {
                        Toast.makeText(baseContext, "Email atau password anda salah", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        signUpBtn.setOnClickListener{
            val intent3 = Intent (this, Register::class.java)
            startActivity(intent3)
        }

    }
}