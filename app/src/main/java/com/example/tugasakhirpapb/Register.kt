package com.example.tugasakhirpapb

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Register : AppCompatActivity(){

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        mAuth = FirebaseAuth.getInstance()

        val btnSignUp : Button = findViewById(R.id.BtnSignUp)
        val inputName : EditText = findViewById(R.id.nameInput)
        val inputPass : EditText = findViewById(R.id.passInput)
        val confirmPass : EditText = findViewById(R.id.confirmPass)
        val inputEmail : EditText = findViewById(R.id.emailInput)

        btnSignUp.setOnClickListener{
            val email = inputEmail.text.toString()
            val pass = inputPass.text.toString()
            val confirm = confirmPass.text.toString()

            mAuth.createUserWithEmailAndPassword(
                email, pass
            ).addOnCompleteListener{ task ->
                val user: FirebaseUser? = mAuth.getCurrentUser()
                user?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")
                        val user = mAuth.currentUser
                        Toast.makeText(
                            baseContext, "Email Verifikasi Telah dikirim",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Autentikasi gagal.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }

    private fun updateUI() {
        val intent = Intent (this, RecyclerView::class.java)
        startActivity(intent)
    }
}