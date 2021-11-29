package com.example.tugasakhirpapb.Model

import java.sql.Blob
import java.sql.Date


data class Konten(val judul : String? = null,
                  val konten_cerita : String? = null,
                  val location : String? = null,
                  val userName : String? = null,
                  val foto : Blob? = null,
                  val like_count : Int? = 0,
                  val created_date : Date? = null){


}