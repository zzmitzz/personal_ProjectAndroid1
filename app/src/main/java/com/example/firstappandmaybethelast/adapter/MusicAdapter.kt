package com.example.firstappandmaybethelast.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstappandmaybethelast.databinding.MusicitemsBinding
import com.example.firstappandmaybethelast.realmdb.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


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
                CoroutineScope(Dispatchers.IO).launch {
                    val url = URL(music.imageResource)
                    val connection = url.openConnection() as HttpURLConnection
                    val myBitmap = BitmapFactory.decodeStream(connection.inputStream)
                    withContext(Dispatchers.Main) {
                        imageView.setImageBitmap(myBitmap)
                    }
                }
                // Query for Realm if this music in the favorite list

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