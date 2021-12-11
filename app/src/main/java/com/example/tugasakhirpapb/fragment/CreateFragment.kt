package com.example.tugasakhirpapb.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.tugasakhirpapb.R
import com.example.tugasakhirpapb.model.Konten
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class CreateFragment : Fragment(R.layout.activity_create_post) {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    private lateinit var database: DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    lateinit var textJudul : EditText
    lateinit var textKonten : EditText
    lateinit var textLocation : EditText
    lateinit var btPost : Button

    lateinit var nama : TextView

    lateinit var filePath : Uri
    lateinit var date : String


    private lateinit var btImage : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_create_post, container, false)
        mAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        textJudul = view.findViewById(R.id.text_Judul)
        textKonten = view.findViewById(R.id.text_Post)
        textLocation = view.findViewById(R.id.text_Location)
        btPost = view.findViewById(R.id.bt_post)
        btImage = view.findViewById(R.id.img_photos)
        nama = view.findViewById(R.id.text_Name)

        database = FirebaseDatabase.getInstance().getReference()

        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


        btPost.setOnClickListener(){
            uploadKonten()
        }

        btImage.setOnClickListener(){
            startFileChooser()
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
                nama.text = "${snapshot.child("nama").value}"

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun uploadKonten() {
        val konten = Konten(
            textJudul.text.toString(),
            textKonten.text.toString(),
            textLocation.text.toString(),
            "",
            "foto_"+textJudul.text.toString(),
            0,
            date
        )
        database.child("konten").child(textJudul.text.toString()).setValue(konten).addOnSuccessListener {
            Toast.makeText(context,"Successfully Posted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(context,"Failed to Post", Toast.LENGTH_SHORT).show()
        }
        uplloadFile()
    }

    fun selectImage(view: View) {
        startFileChooser()
    }

    private fun uplloadFile() {
        if (filePath!=null){
            var pd = ProgressDialog(context)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef : StorageReference = FirebaseStorage.getInstance().reference.child("konten_images").child("foto_"+textJudul.text.toString())
            imageRef.putFile(filePath)
                .addOnSuccessListener { p0 ->
                    pd.dismiss()
                    Toast.makeText(context?.applicationContext,"File Uploaded", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {p0 ->
                    pd.dismiss()
                    Toast.makeText(context?.applicationContext,p0.message, Toast.LENGTH_LONG).show()
                }.addOnProgressListener { p0 ->
                    var progress : Double = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}%")
                }
        }
    }

    private fun startFileChooser() {
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==111 && resultCode == Activity.RESULT_OK && data != null){
            filePath = data.data!!
        }
    }


}