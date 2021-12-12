package com.example.tugasakhirpapb

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
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
        val checkbox : CheckBox = findViewById(R.id.checkBox)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        loginBtn.setOnClickListener{
            val inputEmail = email.text.toString()
            val inputPass = pass.text.toString()
            if(inputEmail.isEmpty()){
                email.setError("Email is Required")
            }
            if(inputPass.isEmpty()){
                pass.setError("Password is Required")
            }else{
                mAuth.signInWithEmailAndPassword(inputEmail, inputPass)
                    .addOnCompleteListener(this){
                        if(it.isSuccessful){
                            val intent2 = Intent (this, MainActivity::class.java)
                            startActivity(intent2)
                        }else {
                            Toast.makeText(baseContext, "Email atau password anda salah", Toast.LENGTH_SHORT).show()
                        }
                    }
            }


        }

        signUpBtn.setOnClickListener{
            val intent3 = Intent (this, Register::class.java)
            startActivity(intent3)
        }

        checkbox.setOnClickListener(){
            if (checkbox.isChecked){
                pass.inputType = 1
            }else{
                pass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

    }
}