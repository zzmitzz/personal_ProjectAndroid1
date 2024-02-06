package com.example.firstappandmaybethelast.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstappandmaybethelast.databinding.MusicitemsBinding
import com.example.firstappandmaybethelast.musicdata.Music

class MusicAdapter: RecyclerView.Adapter<MusicAdapter.ViewHolderMusic>() {

    private var musicList = emptyList<Music>()
    var onItemClick: ((Int) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setMusic(musicListNew: List<Music>){
        musicList = musicListNew
        notifyDataSetChanged() // It will re execute getSize and binder function below whenever data change
    }
    class ViewHolderMusic(
        private val binding: MusicitemsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun binder(music: Music) {
            binding.run {
                textView.text = music.title
                textView2.text = music.artist
                imageView.setImageResource(music.imageResource)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMusic {
        val binding = MusicitemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolderMusic(binding)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onBindViewHolder(holder: ViewHolderMusic, position: Int) {
        holder.binder(musicList[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(position)
        }
    }
}