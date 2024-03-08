package com.example.firstappandmaybethelast.musicdata

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("playlistIds")
    val playlistIds: List<String>,
    @SerializedName("profilePictureUrl")
    val profilePictureUrl: String,
    @SerializedName("favoriteMusic")
    val favoriteMusic: List<String>,
    @SerializedName("recentPlay")
    val recentPlay: List<String>,
): Serializable