package com.example.tugasakhirpapb

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    lateinit var btLike : Button
     var imgName: String = ""
    lateinit var imgKonten : ImageView
    private lateinit var auth: FirebaseAuth
    private val storageReference = FirebaseStorage.getInstance().getReference("konten_images")
    var like :Int = 0

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
        imgCreatePost = findViewById(R.id.bt_createPost)
        imgKonten = findViewById(R.id.img_konten)
        btLike = findViewById(R.id.bt_like)


        val bundle : Bundle?=intent.extras
        val judul = bundle!!.getString("judul").toString()


        database.child("konten").child(judul).get().addOnSuccessListener {
            imgName ="${it.child("foto").value}"
            downloadImage(imgName)
            textJudul.text="${it.child("judul").value}"
            textKonten.text="${it.child("konten_cerita").value}"
            textLokasi.text="${it.child("location").value}"
            textLike.text="${it.child("like_count").value}"
            like = "${it.child("like_count").value}".toInt()

        }.addOnFailureListener{
            Toast.makeText(this,"Failed to Get Data", Toast.LENGTH_SHORT).show()
        }


//        val storageref = FirebaseStorage.getInstance().reference.child("konten_images").child("test3")
//        val localfile = File.createTempFile("tempImage","jpg")
//        storageref.getFile(localfile).addOnSuccessListener {
//
//            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
//            imgKonten.setImageBitmap(bitmap)
//        }.addOnFailureListener{ exception ->
//            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
//        }

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
    }
    /**
     * Fungsi download gambar dari firebase storage
     */
    private fun downloadImage(foto: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = storageReference.child(foto).getBytes(maxDownloadSize).await()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            withContext(Dispatchers.Main) {
                binding.imgKonten.load(bitmap){
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(10F))
                }

            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
//                binding.textJudul.error = e.message
//                binding.progressBarLoadingIndicator.visibility = View.GONE
                Toast.makeText(applicationContext,e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setImageViewHome() {
        binding.imgKonten.load(ContextCompat.getDrawable(this, R.drawable.shape)){
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(10F))
        }
    }

    fun intentCreate(view: View){
        val intent= Intent(this,CreatePost::class.java)
        startActivity(intent)
    }
}