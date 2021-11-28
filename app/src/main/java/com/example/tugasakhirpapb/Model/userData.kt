package com.example.tugasakhirpapb.Model

import java.sql.Blob
import java.util.*

data class userData (val nama : String,
                     val email : String,
                     val password : String,
                     val id : String,
                     val tglLahir : Date,
                     val alamat : String,
                     val fotoProfil : Blob
                     ) {
}