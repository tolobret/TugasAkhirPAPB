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
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.example.tugasakhirpapb.MainActivity
import com.example.tugasakhirpapb.R
import com.example.tugasakhirpapb.Register
import com.example.tugasakhirpapb.model.Konten
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

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
    lateinit var imgProfile : ImageView

    lateinit var nama : TextView

    lateinit var filePath : Uri
    lateinit var date : String

    private var pressedTime = 0L



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
        imgProfile= view.findViewById(R.id.img_profile)

        database = FirebaseDatabase.getInstance().getReference()

        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


        btPost.setOnClickListener(){
            uploadKonten()
        }

        btImage.setOnClickListener(){
            startFileChooser()
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

        database = FirebaseDatabase.getInstance().getReference("userData")
        database.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nama.text = "${snapshot.child("nama").value}"
                val foto = "${snapshot.child("fotoProfil").value}"
                try {
                    Glide.with(this@CreateFragment)
                        .load(foto)
                        .centerCrop()
                        .placeholder(R.drawable.profile_icon)
                        .into(imgProfile)
                }
                catch (e: Exception){

                }

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

            uplloadFile()

//            Toast.makeText(context,"Successfully Posted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(context,"Failed to Post", Toast.LENGTH_SHORT).show()
        }
//        uplloadFile()
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
                    val uriTask: Task<Uri> = p0.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val uploadedImageUri = "${uriTask.result}"

                    updateUrl(uploadedImageUri)

//                    Toast.makeText(context?.applicationContext,"File Uploaded", Toast.LENGTH_LONG).show()

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

    private fun updateUrl(uploadedImageUri: String) {
        val hashMap: HashMap<String, Any> = HashMap()
        if (filePath != null){
            hashMap["urlFoto"] = uploadedImageUri
        }

        val reference = FirebaseDatabase.getInstance().getReference("konten")
        reference.child(textJudul.text.toString())
            .updateChildren(hashMap)
            .addOnSuccessListener {

                Toast.makeText(context, "Successfully Posted", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)

            }
            .addOnFailureListener(){e->
                Toast.makeText(context, "Gagal update URL ke ${e.message}", Toast.LENGTH_SHORT).show()
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