package com.example.tugasakhirpapb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.tugasakhirpapb.model.userData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.core.content.ContextCompat.startActivity as startActivity

class Profile : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        namaAkun = findViewById(R.id.namaAkun)
        nama = findViewById(R.id.Nama1)
        email = findViewById(R.id.Email1)
        nomor = findViewById(R.id.Nomor1)
        tanggal = findViewById(R.id.Date1)
        alamat = findViewById(R.id.Address1)
        password = findViewById(R.id.Pw1)
        fotoProfile = findViewById(R.id.fotoProfile)
        btnSO = findViewById(R.id.btnSignOut)
        btnEdit = findViewById(R.id.editProfile)
        btnBack = findViewById(R.id.back1)

        mAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        btnBack.setOnClickListener(){
            onBackPressed()
        }

        btnEdit.setOnClickListener(){
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }
    }


    private fun loadUserInfo() {
        val user = mAuth.currentUser
        database = FirebaseDatabase.getInstance().getReference("userData")
        database.child("userData").addValueEventListener(object : ValueEventListener{
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

                try {
                    Glide.with(this@Profile)
                        .load(fotoProfile)
                        .placeholder(R.drawable.ellipse_20)
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
