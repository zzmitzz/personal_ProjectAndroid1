package com.example.firstappandmaybethelast.musicdata

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Album (
    @SerializedName("_id")
    val id: String,
    @SerializedName("name_Album")
    val nameAlbum: String,
    @SerializedName("tracks")
    val tracks: List<String>,
    @SerializedName("image")
    val image: String
): Serializable

