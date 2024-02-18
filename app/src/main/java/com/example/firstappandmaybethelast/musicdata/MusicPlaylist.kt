package com.example.firstappandmaybethelast.musicdata

data class MusicPlaylist (
    val id: String,
    val title: String,
    val duration: Long,
    val imageSource: Int,
    val musicList: List<Music> = MusicData.musicList
)