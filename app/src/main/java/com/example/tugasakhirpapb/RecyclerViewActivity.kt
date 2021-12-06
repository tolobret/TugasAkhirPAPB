package com.example.tugasakhirpapb

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.tugasakhirpapb.databinding.ActivityHalamanKontenBinding
import com.example.tugasakhirpapb.databinding.KontenViewBinding
import com.example.tugasakhirpapb.model.Konten
import com.example.tugasakhirpapb.viewHolder.KontenAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var recycler : RecyclerView
    private lateinit var kontenList:ArrayList<Konten>
    private lateinit var database : DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var kontenAdapter : KontenAdapter
    var imgName: String = ""
    lateinit var imgKonten : ImageView
    lateinit var judul : TextView

    private val storageReference = FirebaseStorage.getInstance().getReference("konten_images")
    private lateinit var binding: KontenViewBinding

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

        judul = findViewById(R.id.judulKonten)
        imgKonten = findViewById(R.id.gambarKonten)

        kontenList = arrayListOf<Konten>()
        getKonten()
    }

    private fun getKonten() {
        mAuth = Firebase.auth
        val currentUser = mAuth.currentUser
        binding = KontenViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().getReference("Konten")

        database.child("konten").child("judul5").get().addOnSuccessListener {
            imgName ="${it.child("foto").value}"
            downloadImage(imgName)
            judul.text="${it.child("judul").value}"
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to Get Data", Toast.LENGTH_SHORT).show()
        }



//        database.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()){
//
//                    for (kontenSnapshot in snapshot.children){
//                        val konten = kontenSnapshot.getValue(Konten::class.java)
//                        kontenList.add(konten!!)
//                    }
//
//                    recycler.adapter = kontenAdapter
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@RecyclerViewActivity, error.message, Toast.LENGTH_SHORT).show()
//            }
//
//        })

    }

    private fun downloadImage(foto: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val maxDownloadSize = 5L * 1024 * 1024
            val bytes = storageReference.child(foto).getBytes(maxDownloadSize).await()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            withContext(Dispatchers.Main) {
                binding.gambarKonten.load(bitmap){
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




}