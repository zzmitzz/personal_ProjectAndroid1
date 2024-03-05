package com.example.firstappandmaybethelast.model

import com.example.firstappandmaybethelast.musicdata.Album
import com.example.firstappandmaybethelast.musicdata.Music
import retrofit2.Retrofit
import retrofit2.http.GET

interface ApiAction {
    @GET("getsong")
    suspend fun getSong(): List<Music>
    @GET("albumdata")
    suspend fun getAlbum(): List<Album>
    companion object {
        fun retrofitService(retrofit: Retrofit): ApiAction {
            return retrofit.create(ApiAction::class.java)
        }
    }
}