package com.example.tugasakhirpapb

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.tugasakhirpapb.databinding.ActivityHalamanKontenBinding
import com.example.tugasakhirpapb.databinding.RecyclerViewBinding
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
    private lateinit var binding: RecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllImage()
    }

    private fun getAllImage() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val images = storageReference.listAll().await()
            val imageUrls = mutableListOf<String>()
            for(image in images.items) {
                val url = image.downloadUrl.await()
                imageUrls.add(url.toString())
            }

            withContext(Dispatchers.Main) {
                val animalAdapter = KontenAdapter(imageUrls)
//                if (animalAdapter.itemCount == 0) {
//                    binding.textView8.text = View.VISIBLE
//                }

                binding.recyclerView.apply {
                    adapter = animalAdapter
                    layoutManager = LinearLayoutManager(this@RecyclerViewActivity)
                }
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
//                binding.progressLoadList.visibility = View.GONE
                Toast.makeText(this@RecyclerViewActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

}