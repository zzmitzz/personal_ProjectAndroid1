package com.example.firstappandmaybethelast.musicdata

import com.example.firstappandmaybethelast.R
object MusicPlaylistHIH {
    val listData =
        List(10) {
            com.example.firstappandmaybethelast.musicdata.MusicPlaylistData(
                id = it.toString(),
                title = "HaoNhan$it",
                duration = 1000,
                imageSource = R.drawable.db,
                musicList = MusicData.musicList
            )

        }
}