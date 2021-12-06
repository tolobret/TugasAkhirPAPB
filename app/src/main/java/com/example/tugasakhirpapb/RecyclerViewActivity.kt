package com.example.tugasakhirpapb

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpapb.Model.Konten
import com.example.tugasakhirpapb.viewHolder.KontenAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var recycler : RecyclerView
    private lateinit var kontenList:ArrayList<Konten>
    private lateinit var database : DatabaseReference
    private lateinit var kontenAdapter : KontenAdapter

    lateinit var judul : TextView
    lateinit var imgKonten : ImageView

    private val storageReference = FirebaseStorage.getInstance().getReference("konten_images")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)
        init()
    }

    private fun init(){
        kontenList = ArrayList()
        kontenAdapter = KontenAdapter(this, kontenList)
        recycler = findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.adapter = kontenAdapter



        kontenList = arrayListOf<Konten>()
        getKonten()
    }

    private fun getKonten() {
        database = FirebaseDatabase.getInstance().getReference("Konten")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    for (kontenSnapshot in snapshot.children){
                        val konten = kontenSnapshot.getValue(Konten::class.java)
                        kontenList.add(konten!!)
                    }

                    recycler.adapter = kontenAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RecyclerViewActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }




}