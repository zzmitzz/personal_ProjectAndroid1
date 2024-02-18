package com.example.firstappandmaybethelast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstappandmaybethelast.databinding.MusicplaylistitemBinding
import com.example.firstappandmaybethelast.musicdata.MusicPlaylist

class MusicListAdapter: RecyclerView.Adapter<MusicListAdapter.VH>() {
    private lateinit var musicList: List<MusicPlaylist>
    @SuppressLint("NotifyDataSetChanged")
    fun setMusic(musicList: List<MusicPlaylist>){
        this.musicList = musicList
        notifyDataSetChanged()
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
        holder.binder(musicList[position])
    }

    override fun getItemCount(): Int = musicList.size

    class VH(private val binding: MusicplaylistitemBinding) : RecyclerView.ViewHolder(binding.root){
        fun binder(musicList: MusicPlaylist){
            binding.imageView.setImageResource(musicList.imageSource)
        }
    }
}