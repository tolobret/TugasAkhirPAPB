package com.example.tugasakhirpapb.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpapb.Model.Konten
import com.example.tugasakhirpapb.R
import com.google.android.material.imageview.ShapeableImageView

class kontenAdapter (private val listKonten : ArrayList<Konten>) :
    RecyclerView.Adapter<kontenAdapter.MyViewHolder>() {

    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.konten_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = listKonten[position]
        val img = currentItem.foto?.toInt()
        holder.judul.text = currentItem.judul
        if (img != null) {
            holder.foto.setImageResource(img)
        }
    }

    override fun getItemCount(): Int {


        return listKonten.size
    }

    class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val judul : TextView = itemView.findViewById(R.id.judulKonten)
        val foto : ShapeableImageView = itemView.findViewById(R.id.gambarKonten)
    }
}