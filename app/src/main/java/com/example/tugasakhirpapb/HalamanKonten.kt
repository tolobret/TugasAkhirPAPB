package com.example.tugasakhirpapb

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.bumptech.glide.Glide
import com.example.tugasakhirpapb.databinding.ActivityHalamanKontenBinding
import com.example.tugasakhirpapb.fragment.CreateFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class HalamanKonten : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityHalamanKontenBinding

    lateinit var textJudul : TextView
    lateinit var textKonten : TextView
    lateinit var textLokasi : TextView
    lateinit var textLike : TextView
    lateinit var imgCreatePost : ImageView
    lateinit var btLike : FloatingActionButton
    lateinit var btnBack : ImageButton
     var imgName: String = ""
    lateinit var imgKonten : ImageView
    private lateinit var auth: FirebaseAuth
    private val storageReference = FirebaseStorage.getInstance().getReference("konten_images")
    var like :Int = 0
    private lateinit var judul : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        binding = ActivityHalamanKontenBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setContentView(R.layout.activity_halaman_konten)
        database = FirebaseDatabase.getInstance().getReference()


        var clicked : Boolean = false

        textJudul = findViewById(R.id.text_judul)
        textKonten = findViewById(R.id.text_konten)
        textLokasi = findViewById(R.id.text_lokasi)
        textLike = findViewById(R.id.text_like)
        imgKonten = findViewById(R.id.img_konten)
        btLike = findViewById(R.id.bt_like)
        btnBack = findViewById(R.id.backBtn)


        val bundle : Bundle?=intent.extras
        judul = bundle!!.getString("judul").toString()


        showImage()



        btLike.setOnClickListener(){

            if (!clicked){
                like+=1
                database = FirebaseDatabase.getInstance().getReference()
                var like = mapOf<String,Int>(
                   "like_count" to like
                )

                database.child("konten").child(judul).updateChildren(like).addOnSuccessListener {
                    Toast.makeText(this,"liked",Toast.LENGTH_SHORT).show()
                    clicked=true
                    database.child("konten").child(judul).get().addOnSuccessListener {
                        textLike.text="${it.child("like_count").value}"

                    }
                }

            }else{
                Toast.makeText(this,"You've Liked This Post",Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
//            val intent = Intent (this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }

    private fun showImage() {
        database.child("konten").child(judul).get().addOnSuccessListener {
            imgName ="${it.child("urlFoto").value}"

            textJudul.text="${it.child("judul").value}"
            textKonten.text="${it.child("konten_cerita").value}"
            textLokasi.text="${it.child("location").value}"
            textLike.text="${it.child("like_count").value}"
            like = "${it.child("like_count").value}".toInt()


            try {
                Glide.with(this@HalamanKonten)
                    .load(imgName)
                    .centerCrop()
                    .placeholder(R.drawable.shape)
                    .into(imgKonten)
                binding.progressBar.visibility = View.GONE
            }
            catch (e: Exception){

            }

        }.addOnFailureListener{
            Toast.makeText(this,"Failed to Get Data", Toast.LENGTH_SHORT).show()
        }
    }


    fun intentMaps(view: View){
        val gmmIntentUri = Uri.parse("geo:0,0?q=${textLokasi.text.toString()}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}