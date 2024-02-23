package com.example.firstappandmaybethelast.model

import com.example.firstappandmaybethelast.ext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {
    private const val BASE_URL = ext.apiMusicResource
//    val gson = GsonBuilder()
//        .registerTypeHierarchyAdapter()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiAction: ApiAction by lazy {
        ApiAction.retrofitService(retrofit)
    }

}