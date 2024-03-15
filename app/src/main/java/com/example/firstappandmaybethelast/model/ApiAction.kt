package com.example.firstappandmaybethelast.model

import com.example.firstappandmaybethelast.musicdata.Album
import com.example.firstappandmaybethelast.musicdata.Artist
import com.example.firstappandmaybethelast.musicdata.Music
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiAction {
    @GET("getsong")
    suspend fun getSong(): List<Music>
    @GET("albumdata")
    suspend fun getAlbum(): List<Album>
    @GET("artistdata")
    suspend fun getArtist(): List<Artist>
    @POST("getsongbyid")
    fun getSongByID(@Body body: Map<@JvmSuppressWildcards String,@JvmSuppressWildcards Any>): Call<ResponseBody>
    @POST("validateauth")
    fun authUser(@Body body:  Map<@JvmSuppressWildcards String,@JvmSuppressWildcards Any>): Call<ResponseBody>

    companion object {
        fun retrofitService(retrofit: Retrofit): ApiAction {
            return retrofit.create(ApiAction::class.java)
        }
    }
}