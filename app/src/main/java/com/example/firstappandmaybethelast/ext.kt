package com.example.firstappandmaybethelast

import android.graphics.Bitmap
import android.util.Log
import com.example.firstappandmaybethelast.model.ServiceLocator
import com.example.firstappandmaybethelast.musicdata.User
import com.example.firstappandmaybethelast.realmdb.Album
import com.example.firstappandmaybethelast.realmdb.Artist
import com.example.firstappandmaybethelast.realmdb.FavoriteMusic
import com.example.firstappandmaybethelast.realmdb.Music
import com.google.gson.Gson
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ext {
    val realm = Realm.open(RealmConfiguration.create(schema = setOf(Artist::class, Music::class, Album::class, FavoriteMusic::class)))
    const val apiMusicResource: String = "https://ap-southeast-1.aws.data.mongodb-api.com/app/data-pkcss/endpoint/"
    var listMusic: List<Music> = getMusicData()
    var customListMusic: List<Music> = emptyList()
    var customAlbumList: MutableList<Album> = mutableListOf()
    var customArtistList: MutableList<Artist> = mutableListOf()
    var user: User? = null
    var bitmapCurrentSong : Bitmap? = null
    private val gson = Gson()
    var isLogin: Boolean = false
    fun getMusicData(): List<Music> = realm.query(Music::class).find().toList()
    fun getAlbumData(): List<Album> = realm.query(Album::class).find().toList()
    fun getArtistData(): List<Artist> = realm.query(Artist::class).find().toList()

    suspend fun getSongByID(id: String): com.example.firstappandmaybethelast.musicdata.Music?{
        val body = mapOf<String,Any>(
            "_id" to id
        )
        var result: com.example.firstappandmaybethelast.musicdata.Music?  = null
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
                        result = music
                    }
                }catch (e : Error){}
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
        withTimeout(1000){
            try {
                while(result == null){
                    delay(100)
                    Log.d("ArtistTest", "Get Song")
                }
            } catch (e: Exception) {
                result = null
            }
        }
        return result
    }
    fun cvtToRealmMusic(music: com.example.firstappandmaybethelast.musicdata.Music): Music{
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
     fun cvtToRealmArtist(artist: com.example.firstappandmaybethelast.musicdata.Artist): Artist {
        return Artist().apply {
            this.id = artist.id
            this.name = artist.name
            this.image = artist.image.trim()
            val listIDMusic: List<String> = artist.music
            this.music.addAll(listIDMusic)
        }
    }

    fun cvtToRealmAlbum(album: com.example.firstappandmaybethelast.musicdata.Album): RealmObject {
        return com.example.firstappandmaybethelast.realmdb.Album().apply {
            this.id = album.id
            this.title = album.nameAlbum
            this.imageSource = album.image.trim()
            val listIDMusic: List<String> = album.tracks
            this.musicList.addAll(listIDMusic)
        }
    }
    suspend fun getArtistByID(){

    }
}