package com.example.firstappandmaybethelast.model

import com.example.firstappandmaybethelast.musicdata.Music
import retrofit2.Retrofit
import retrofit2.http.GET

interface ApiAction {
    @GET("getsong")
    suspend fun getSong(): List<Music>

    companion object {
        fun retrofitService(retrofit: Retrofit): ApiAction {
            return retrofit.create(ApiAction::class.java)
        }
    }
}