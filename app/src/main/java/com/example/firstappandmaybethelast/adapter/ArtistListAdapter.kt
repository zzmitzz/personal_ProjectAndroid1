package com.example.firstappandmaybethelast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstappandmaybethelast.R
import com.example.firstappandmaybethelast.databinding.ArtistItemBinding
import com.example.firstappandmaybethelast.realmdb.Artist
import com.squareup.picasso.Picasso

class ArtistListAdapter: RecyclerView.Adapter<ArtistListAdapter.ArtistViewHolder>() {
    private var musicList = emptyList<Artist>()
     var onItemClick : ((Int) -> Unit)? = null
    fun setData(list: List<Artist>){
        this.musicList = list
        notifyDataSetChanged()
    }
    class ArtistViewHolder(
        private val binding: ArtistItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun binder(artist: Artist){
            binding.run {
                this.textView.text = artist.name
                Picasso.get()
                    .load(artist.image)
                    .resize(150,150)
                    .centerCrop()
                    .error(R.drawable.icon)
                    .into(binding.imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding = ArtistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ArtistViewHolder(binding)
    }

    override fun getItemCount(): Int = musicList.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.binder(musicList[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }
}