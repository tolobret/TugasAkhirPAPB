package com.example.tugasakhirpapb.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasakhirpapb.HalamanKonten
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
import kotlin.system.exitProcess


class HomeFragment : Fragment(R.layout.recycler_view) {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    private lateinit var database : DatabaseReference
    private lateinit var kontenArrayList : ArrayList<Konten>


    private val storageReference = FirebaseStorage.getInstance().getReference("konten_images")
    private lateinit var binding: RecyclerViewBinding
    private var pressedTime = 0L
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

        val callback= object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (pressedTime+2000> System.currentTimeMillis()){

                    exitProcess(-1)
                }else{
                    Toast.makeText(context,"Press back again to exit app", Toast.LENGTH_SHORT).show()
                }
                pressedTime = System.currentTimeMillis()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

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
                val kontenAdapter = KontenAdapter(imageUrls,kontenArrayList)
                if (kontenAdapter.itemCount == 0) {
                    binding.progressBar2.visibility = View.VISIBLE
                }


                binding.recyclerView.apply {
                    adapter = kontenAdapter
                    layoutManager = LinearLayoutManager(context)
                }

                binding.progressBar2.visibility = View.GONE
                kontenAdapter.setOnItemClickListener(object : KontenAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
//                        Toast.makeText(this@RecyclerViewActivity,"Clicked No : $position",Toast.LENGTH_LONG).show()
                        val intent = Intent(context,HalamanKonten::class.java)
                        intent.putExtra("judul",kontenArrayList[position].judul)
                        startActivity(intent)
                    }

                })
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