package com.example.tugasakhirpapb.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.tugasakhirpapb.model.Konten
import com.example.tugasakhirpapb.databinding.KontenViewBinding
import com.example.tugasakhirpapb.R

import com.google.android.material.imageview.ShapeableImageView

class KontenAdapter(private val ImageUrl: List<String>) : RecyclerView.Adapter<KontenAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: KontenViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            with(binding) {
                imageKonten.load(url){
                    crossfade(true)
                    crossfade(500)
                    transformations(RoundedCornersTransformation(10F))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding  = KontenViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageUrl[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return ImageUrl.size
    }



}