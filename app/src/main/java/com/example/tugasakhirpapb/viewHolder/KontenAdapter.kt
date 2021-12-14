package com.example.tugasakhirpapb.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.example.tugasakhirpapb.model.Konten
import com.example.tugasakhirpapb.databinding.KontenViewBinding
import com.example.tugasakhirpapb.R

import com.google.android.material.imageview.ShapeableImageView

class KontenAdapter(private val ImageUrl: List<String>,
                    private val kontenList : ArrayList<Konten>
                    ) : RecyclerView.Adapter<KontenAdapter.ViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }

    inner class ViewHolder(private val binding: KontenViewBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        val judul : TextView = itemView.findViewById(R.id.textJudul)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding  = KontenViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding,mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageUrl[position].let {



            try {
                Glide.with(holder.itemView)
                    .load(it)
                    .centerCrop()
                    .placeholder(R.drawable.shape)
                    .into(holder.itemView.findViewById(R.id.imageKonten))
            }
            catch (e: Exception){

            }

        }
        val currentitem = kontenList[position]
        holder.judul.text =currentitem.judul
    }

    override fun getItemCount(): Int {
        return ImageUrl.size
    }



}