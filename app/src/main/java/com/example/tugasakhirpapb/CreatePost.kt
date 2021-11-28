package com.example.tugasakhirpapb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tugasakhirpapb.Model.Konten
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreatePost : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    lateinit var textJudul : EditText
    lateinit var textKonten : EditText
    lateinit var textLocation : EditText
    lateinit var btPost : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        textJudul = findViewById(R.id.text_Judul)
        textKonten = findViewById(R.id.text_Post)
        textLocation = findViewById(R.id.text_Location)
        btPost = findViewById(R.id.bt_post)


        database = FirebaseDatabase.getInstance().getReference()



        btPost.setOnClickListener(){
            val konten = Konten(
                textJudul.text.toString(),
                textKonten.text.toString(),
                textLocation.text.toString()
            )
            database.child("konten").child(textJudul.text.toString()).setValue(konten).addOnSuccessListener {
                Toast.makeText(this,"Successfully Posted", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Failed to Post",Toast.LENGTH_SHORT).show()
            }
        }
    }
}