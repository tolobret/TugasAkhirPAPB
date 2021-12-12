package com.example.tugasakhirpapb

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.tugasakhirpapb.fragment.ProfileFragment
import com.example.tugasakhirpapb.model.Konten
import com.example.tugasakhirpapb.model.userData
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfile : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    lateinit var namaAkun : EditText
    lateinit var editEmail : EditText
    lateinit var editNo : EditText
    lateinit var editTgl : EditText
    lateinit var editAlamat : EditText
    lateinit var editPw : EditText
    lateinit var saveBtn : Button
    lateinit var btnBack : ImageView
    lateinit var fotoProfile : ImageView
    var filePath: Uri?= null
    lateinit var progressDialog: ProgressDialog
    var userId : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        namaAkun = findViewById(R.id.namaAkun)
        editEmail = findViewById(R.id.editEmail)
        editNo = findViewById(R.id.editNo)
        editTgl = findViewById(R.id.editTgl)
        editAlamat = findViewById(R.id.editAlamat)
        editPw = findViewById(R.id.editPw)
        saveBtn = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.back1)
        fotoProfile = findViewById(R.id.fotoProfile)


        mAuth = FirebaseAuth.getInstance()


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Tunggu")
        progressDialog.setCanceledOnTouchOutside(false)


        val user = mAuth.currentUser
        user?.let {
            userId = user.uid
        }

        loadUserInfo()


        btnBack.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fotoProfile.setOnClickListener(){
            showImageAttachMenu()
        }

        saveBtn.setOnClickListener(){
            if (filePath != null){
                uploadImage()

            }


            val Nama = namaAkun.text.toString()
            val Email = editEmail.text.toString()
            val No = editNo.text.toString()
            val Tgl = editTgl.text.toString()
            val Alamat = editAlamat.text.toString()
            val Pw = editPw.text.toString()



            var update = mapOf<String,String>(
                "email" to Email,
                "nomor" to No,
                "tglLahir" to Tgl,
                "alamat" to Alamat
            )

            database.child(userId).updateChildren(update).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(baseContext,"Succes to update profile", Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(baseContext,"Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }


        }

    }


    private fun uploadImage() {
        progressDialog.setMessage("Uploading profile image")
        progressDialog.show()

        val filePathAndName = "ProfileImages/"+ mAuth.currentUser!!.uid

        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(filePath!!)
            .addOnSuccessListener { taskSnapshot->

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUri = "${uriTask.result}"

                updateProfile(uploadedImageUri)
            }
            .addOnFailureListener(){e->
                progressDialog.dismiss()
                Toast.makeText(this, "Gagal upload image ke ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


//     cek lagi

    private fun updateProfile(uploadedImageUri: String) {
        progressDialog.setMessage("Updating profile..")

        val hashMap: HashMap<String, Any> = HashMap()
//        hashMap["nama"] = "$namaAkun"
        if (filePath != null){
            hashMap["fotoProfil"] = uploadedImageUri
        }

        val reference = FirebaseDatabase.getInstance().getReference("userData")
        reference.child(userId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Profil berhasil diupdate", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener(){e->
                progressDialog.dismiss()
                Toast.makeText(this, "Gagal update profile ke ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {

        database = FirebaseDatabase.getInstance().getReference("userData")
        database.child(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val Foto = "${snapshot.child("fotoProfil").value}"

                val nama = "${snapshot.child("nama").value}"
                var Email = "${snapshot.child("email").value}"
                val Nomor = "${snapshot.child("nomor").value}"
                val Tanggal = "${snapshot.child("tglLahir").value}"
                var Alamat = "${snapshot.child("alamat").value}"
                val Password = "${snapshot.child("password").value}"

                namaAkun.setText(nama)
                editEmail.setText(Email)
                editNo.setText(Nomor)
                editTgl.setText(Tanggal)
                editAlamat.setText(Alamat)
                editPw.setText(Password)

                try {
                    Glide.with(this@EditProfile)
                        .load(Foto)
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

    private fun showImageAttachMenu(){
        val popUp = PopupMenu(this, fotoProfile)
        popUp.menu.add(Menu.NONE, 0, 0, "Camera")
        popUp.menu.add(Menu.NONE, 1, 1, "Gallery")
        popUp.show()

        popUp.setOnMenuItemClickListener {item->

            val id = item.itemId
            if(id == 0) {
                pickImageCamera()
            }else if (id==1) {
                pickImageGalerry()
            }

            true
            }
        }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        filePath = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath)
        cameraActivityResultLauncher.launch(intent)
    }

    private fun pickImageGalerry() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                //filePath = data!!.data

                fotoProfile.setImageURI(filePath)
            }else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                filePath = data!!.data

                fotoProfile.setImageURI(filePath)
            }else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    )
}