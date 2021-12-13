package com.example.tugasakhirpapb.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.example.tugasakhirpapb.EditProfile
import com.example.tugasakhirpapb.Login
import com.example.tugasakhirpapb.MainActivity
import com.example.tugasakhirpapb.R
import com.example.tugasakhirpapb.databinding.ActivityProfileBinding
import com.example.tugasakhirpapb.databinding.RecyclerViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess

class ProfileFragment : Fragment(R.layout.activity_profile) {

    private lateinit var database: DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    lateinit var nama : TextView
    lateinit var email: TextView
    lateinit var nomor : TextView
    lateinit var tanggal : TextView
    lateinit var alamat : TextView
    lateinit var password : TextView
    lateinit var fotoProfile : ImageView
    lateinit var btnSO : Button
    lateinit var btnEdit : ImageView
    lateinit var btnBack : ImageView
    private val storageReference = FirebaseStorage.getInstance().getReference("ProfileImages")
    private lateinit var binding: ActivityProfileBinding
    private var pressedTime = 0L

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//    }
//
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        nama = view.findViewById(R.id.namaAkun)
        email = view.findViewById(R.id.Email1)
        nomor = view.findViewById(R.id.Nomor1)
        tanggal = view.findViewById(R.id.Date1)
        alamat = view.findViewById(R.id.Address1)
        password = view.findViewById(R.id.Pw1)
        fotoProfile = view.findViewById(R.id.fotoProfile)
        btnSO = view.findViewById(R.id.btnSignOut)
        btnEdit = view.findViewById(R.id.editProfile)
        btnBack = view.findViewById(R.id.back1)

        mAuth = FirebaseAuth.getInstance()
        var userId : String = ""
        val user = mAuth.currentUser
        user?.let {
            userId = user.uid
        }


        loadUserInfo()


        btnEdit.setOnClickListener(){
            val intent = Intent(context, EditProfile::class.java)
            startActivity(intent)
        }
    btnBack.setOnClickListener(){
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    btnSO.setOnClickListener(){
        mAuth.signOut()
        val intent = Intent(context, Login::class.java)
        startActivity(intent)

    }

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

        return view
    }




    private fun loadUserInfo() {

        var userId : String = ""
        val user = mAuth.currentUser
        user?.let {
            userId = user.uid
        }
//        downloadImage(userId)
        database = FirebaseDatabase.getInstance().getReference("userData")
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val Foto = "${snapshot.child("fotoProfil").value}"
                val Nama = "${snapshot.child("nama").value}"
                var Email = "${snapshot.child("email").value}"
                val Nomor = "${snapshot.child("nomor").value}"
                val Tanggal = "${snapshot.child("tglLahir").value}"
                var Alamat = "${snapshot.child("alamat").value}"
                val Password = "${snapshot.child("password").value}"

                nama.text = Nama.toString()
                email.text = Email.toString()
                nomor.text = Nomor.toString()
                tanggal.text = Tanggal.toString()
                alamat.text = Alamat.toString()
                password.text = Password.toString()




                try {
                    Glide.with(this@ProfileFragment)
                        .load(Foto)
                        .centerCrop()
                        .placeholder(R.drawable.profile_icon)
                        .into(fotoProfile)
                }
                catch (e: Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }


}