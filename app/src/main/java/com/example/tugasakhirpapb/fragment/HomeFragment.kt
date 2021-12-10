package com.example.tugasakhirpapb.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasakhirpapb.R
import com.example.tugasakhirpapb.RecyclerViewActivity
import com.example.tugasakhirpapb.databinding.RecyclerViewBinding
import com.example.tugasakhirpapb.model.Konten
import com.example.tugasakhirpapb.viewHolder.KontenAdapter
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(R.layout.recycler_view) {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    private lateinit var database : DatabaseReference
    private lateinit var kontenArrayList : ArrayList<Konten>

    private val storageReference = FirebaseStorage.getInstance().getReference("konten_images")
    private lateinit var binding: RecyclerViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = RecyclerViewBinding.inflate(layoutInflater)
        val view =  inflater.inflate(R.layout.recycler_view, container, false)

        kontenArrayList = arrayListOf<Konten>()
        getUserData()
        getAllImage()

        return binding.root
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
                val animalAdapter = KontenAdapter(imageUrls,kontenArrayList)
//                if (animalAdapter.itemCount == 0) {
//                    binding.textView8.text = View.VISIBLE
//                }

                binding.recyclerView.apply {
                    adapter = animalAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
//                binding.progressLoadList.visibility = View.GONE
//                Toast.makeText(this@HomeFragment, e.message, Toast.LENGTH_LONG).show()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()

            }
        }
    }


    private fun getUserData() {

        database = FirebaseDatabase.getInstance().getReference("konten")

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){


                        val konten = userSnapshot.getValue(Konten::class.java)
                        kontenArrayList.add(konten!!)

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }



}