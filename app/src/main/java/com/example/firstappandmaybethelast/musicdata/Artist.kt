package com.example.firstappandmaybethelast.musicdata

data class Artist(
    val name: String,
    val born: String,
    val description: String,
    val imageSource: Int,
    val genre: String,
    val country: String,
    val music: List<Music>
)