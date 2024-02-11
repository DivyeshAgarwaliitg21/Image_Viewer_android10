package com.example.imagesorter3// com.example.imagesorter3.ImageAdapter.kt

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.imagesorter3.databinding.ItemImageBinding

data class ImageAdapter(val context: Context, val imglist: List<Picmodel>) : RecyclerView.Adapter<ImageAdapter.Viewholder>() {

    class Viewholder(binding: ItemImageBinding) : ViewHolder(binding.root) {
        val img = binding.image
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent,false)
        return Viewholder(ItemImageBinding.bind(view))
    }
    override fun getItemCount(): Int = imglist.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val model = imglist[position]
        println(context)
        Glide.with(context).load(model.uri).into(holder.img)
    }
}
