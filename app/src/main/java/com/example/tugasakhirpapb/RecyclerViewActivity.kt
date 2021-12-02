package com.example.tugasakhirpapb

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpapb.Model.Konten
import com.example.tugasakhirpapb.viewHolder.kontenAdapter
import com.google.firebase.database.*

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var recycler : RecyclerView
    private lateinit var kontenList:ArrayList<Konten>
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)

        init()
    }

    private fun init(){
        recycler = findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)


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

                    recycler.adapter = kontenAdapter(kontenList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())

            }

        })

    }




}