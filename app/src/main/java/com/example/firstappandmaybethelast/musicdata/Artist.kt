package com.example.firstappandmaybethelast.musicdata

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Artist(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name_Artist")
    val name: String,
    val image: String,
    @SerializedName("id_albums")
    val music: List<String>
): Serializable