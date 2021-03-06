package com.example.tugasakhirpapb

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.tugasakhirpapb.model.Konten
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class CreatePost : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    lateinit var textJudul : EditText
    lateinit var textKonten : EditText
    lateinit var textLocation : EditText
    lateinit var btPost : Button
    lateinit var btHome : ImageView
    lateinit var imgPhoto : ImageView

    lateinit var filePath : Uri
    lateinit var date : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        textJudul = findViewById(R.id.text_Judul)
        textKonten = findViewById(R.id.text_Post)
        textLocation = findViewById(R.id.text_Location)
        btPost = findViewById(R.id.bt_post)
        imgPhoto = findViewById(R.id.img_photos)

        database = FirebaseDatabase.getInstance().getReference()

        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())


        btPost.setOnClickListener(){
            uploadKonten()

        }

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
            Toast.makeText(this,"Successfully Posted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to Post",Toast.LENGTH_SHORT).show()
        }
        uplloadFile()
    }

//    fun selectImage(view: View) {
//        startFileChooser()
//    }

    fun buttonHome(view: View) {
        val intent = Intent(this,RecyclerViewActivity::class.java)
        startActivity(intent)
    }

    private fun uplloadFile() {
        if (filePath!=null){
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef : StorageReference = FirebaseStorage.getInstance().reference.child("konten_images").child("foto_"+textJudul.text.toString())
            imageRef.putFile(filePath)
                .addOnSuccessListener { p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext,"File Uploaded",Toast.LENGTH_LONG).show()
                    val intent = Intent(this,RecyclerViewActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()
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