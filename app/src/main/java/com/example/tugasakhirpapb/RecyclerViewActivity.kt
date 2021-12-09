package com.example.tugasakhirpapb

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasakhirpapb.databinding.RecyclerViewBinding
import com.example.tugasakhirpapb.fragment.CreateFragment
import com.example.tugasakhirpapb.fragment.HomeFragment
import com.example.tugasakhirpapb.fragment.ProfileFragment
import com.example.tugasakhirpapb.model.Konten
import com.example.tugasakhirpapb.viewHolder.KontenAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.tugasakhirpapb.CreatePost
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private lateinit var kontenArrayList : ArrayList<Konten>

    private val storageReference = FirebaseStorage.getInstance().getReference("konten_images")
    private lateinit var binding: RecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val homeFragment = HomeFragment()
//        val profileFragment = ProfileFragment()
//        val createPostFragment = CreateFragment()
//        var bottomNav : BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        kontenArrayList = arrayListOf<Konten>()
        getUserData()
        getAllImage()


//        bottomNav.setOnItemSelectedListener { item ->
//            when(item.itemId){
//                R.id.homeFragment -> replaceFragment(homeFragment)
//                R.id.profileFragment -> replaceFragment(profileFragment)
//                R.id.createFragment -> replaceFragment(createPostFragment)
//            }
//            true
//        }
    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()

        }
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


    private fun getUserData() {

        database = FirebaseDatabase.getInstance().getReference("konten")

        database.addValueEventListener(object : ValueEventListener{

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