package com.example.tugasakhirpapb.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.tugasakhirpapb.EditProfile
import com.example.tugasakhirpapb.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment(R.layout.activity_profile) {

    private lateinit var database: DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    lateinit var namaAkun : TextView
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
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        namaAkun = view.findViewById(R.id.namaAkun)
        nama = view.findViewById(R.id.Nama1)
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
        loadUserInfo()

        btnEdit.setOnClickListener(){
            val intent = Intent(context, EditProfile::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadUserInfo() {
        var userId : String = ""
        val user = mAuth.currentUser
        user?.let {
            userId = user.uid
        }
        database = FirebaseDatabase.getInstance().getReference("userData")
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val Foto = "${snapshot.child("fotoProfil").value}"
                val Nama = "${snapshot.child("akunUser").value}"
                val Username = "${snapshot.child("nama").value}"
                var Email = "${snapshot.child("email").value}"
                val Nomor = "${snapshot.child("nomor").value}"
                val Tanggal = "${snapshot.child("tglLahir").value}"
                var Alamat = "${snapshot.child("alamat").value}"
                val Password = "${snapshot.child("password").value}"

                namaAkun.text = Nama.toString()
                nama.text = Username.toString()
                email.text = Email.toString()
                nomor.text = Nomor.toString()
                tanggal.text = Tanggal.toString()
                alamat.text = Alamat.toString()
                password.text = Password.toString()

//                try {
//                    Glide.with(this@Profile)
//                        .load(fotoProfile)
//                        .placeholder(R.drawable.ellipse_20)
//                        .into(fotoProfile)
//                }
//                catch (e: Exception){
//
//                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}