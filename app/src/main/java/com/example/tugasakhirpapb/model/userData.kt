package com.example.tugasakhirpapb.model

import java.sql.Blob
import java.util.*

data class userData (val nama : String,
                     val email : String,
                     val password : String,
                     val tglLahir : Date? = null,
                     val alamat : String? = null,
                     val fotoProfil : Blob? = null
                     ) {
}