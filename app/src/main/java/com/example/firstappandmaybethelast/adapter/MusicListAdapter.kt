package com.example.firstappandmaybethelast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.databinding.MusicplaylistitemBinding
import com.example.firstappandmaybethelast.realmdb.Album
import com.squareup.picasso.Picasso

class MusicListAdapter() : RecyclerView.Adapter<MusicListAdapter.VH>() {
    private var albumList: MutableList<Album> = emptyList<Album>().toMutableList()
    public var itemClick: ((Int) -> (Unit))? = null
    init {
        this.albumList.addAll(albumList)
    }
    fun setList(album: MutableList<Album>){
        this.albumList = album
    }
    class VH(private val binding: MusicplaylistitemBinding) : RecyclerView.ViewHolder(binding.root){
        fun binder(album: Album){
            Picasso.get()
                .load(album.imageSource)
                .resize(150,150)
                .centerCrop()
                .error(R.drawable.icon)
                .into(binding.imageView)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = MusicplaylistitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binder(albumList[position])
        holder.itemView.setOnClickListener {
            itemClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int = albumList.size


}