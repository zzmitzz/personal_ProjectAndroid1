package com.example.firstappandmaybethelast.musicdata

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Music(
    @SerializedName("_id")
    val id: String,
    val title: String,
    val artist: String,
    val genre: String,
    @SerializedName("link")
    val musicSource: String,
    @SerializedName("imagecover")
    val imageResource: String,
) : Serializable
