package com.example.tugasakhirpapb.model


data class Konten(val judul : String,
                  val konten_cerita : String,
                  val location : String,
                  val userName : String? = null,
                  val foto : String,
                  val like_count : Int = 0,
                  val created_date : String){


}