package com.example.tugasakhirpapb.model

data class userData(

    val nama: String,
    val email: String,
    val password: String,
    val tglLahir: String? = null,
    val alamat: String? = null,
    val nomor: String? = null,
    val fotoProfil: String? = null,
    val akunUser: String?=null,
                     ) {
}