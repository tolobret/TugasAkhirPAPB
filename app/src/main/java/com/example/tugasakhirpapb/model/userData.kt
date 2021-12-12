package com.example.tugasakhirpapb.model

data class userData(

    val nama: String,
    val email: String,
    val password: String,
    val tglLahir: String? = "",
    val alamat: String? = "",
    val nomor: String? = "",
    val fotoProfil: String? = null,
    val akunUser: String?=null,
                     ) {
}