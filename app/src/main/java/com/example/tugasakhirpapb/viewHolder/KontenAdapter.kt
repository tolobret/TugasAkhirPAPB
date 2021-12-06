package com.example.tugasakhirpapb.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirpapb.model.Konten
import com.example.tugasakhirpapb.databinding.KontenViewBinding
import com.example.tugasakhirpapb.R

import com.google.android.material.imageview.ShapeableImageView

class KontenAdapter(var c: Context, var kontenList : ArrayList<Konten>)
    : RecyclerView.Adapter<KontenAdapter.KontenViewHolder>()
{

    inner class KontenViewHolder(var v:KontenViewBinding):RecyclerView.ViewHolder(v.root){}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontenViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<KontenViewBinding>(itemView, R.layout.konten_view, parent,false)
        return KontenViewHolder(v)
    }

    override fun onBindViewHolder(holder: KontenViewHolder, position: Int) {
        holder.v.isKonten = kontenList[position]
        

    }

    override fun getItemCount(): Int {

        return kontenList.size
    }

}