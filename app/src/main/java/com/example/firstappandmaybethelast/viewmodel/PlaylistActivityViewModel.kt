package com.example.firstappandmaybethelast.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.model.ServiceLocator
import com.example.firstappandmaybethelast.realmdb.Album
import com.example.firstappandmaybethelast.realmdb.Music
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistActivityViewModel : ViewModel() {
    private var _adapter = MusicAdapter()
    val adapter: MusicAdapter
        get() = _adapter
    var ready = MutableStateFlow(false)
    private lateinit var album: Album
    var listMusic: MutableList<Music> = mutableListOf()
    private val gson = Gson()
    fun viewModelShowMusic() {
        // get Music data through API,
        CoroutineScope(Dispatchers.IO).launch{
            for(id in album.musicList){
                val body = mapOf<String,Any>(
                    "_id" to id
                )
                ServiceLocator.apiAction.getSongByID(body).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        var response = response.body()?.string()
                        if (response != null) {
                            response = response.substring(10,response.length-1)
                        }
                        try{
                            if(response != null){
                                val music: com.example.firstappandmaybethelast.musicdata.Music? = gson.fromJson(response, com.example.firstappandmaybethelast.musicdata.Music::class.java)
                                music?.let { cvtToRealmMusic(it) }?.let { listMusic.add(it) }
                                if (music != null) {
                                    Log.d("PlaylistActivity", "" + music.title + music.imageResource)
                                }
                            }
                        }catch (e : Error){}
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    }

                })
            }
            delay(2000)
            Log.d("PlaylistActivity", "Done")
            withContext(Dispatchers.Main){_adapter.setMusic(listMusic)}
            ext.customListMusic = listMusic
            ready.update {true}
            }
        }
    fun setCurrentAlbum(album: Album){
        this.album = album
    }
    private fun cvtToRealmMusic(music: com.example.firstappandmaybethelast.musicdata.Music): Music{
        return Music().apply {
            _id = music.id
            title = music.title
            artist = music.artist
            genre = music.genre
            musicSource = music.musicSource
            imageResource = music.imageResource
            isFavorite = false
        }
    }
}
