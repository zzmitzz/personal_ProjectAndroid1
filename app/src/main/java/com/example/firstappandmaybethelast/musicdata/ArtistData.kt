package com.example.firstappandmaybethelast.musicdata

import com.example.firstappandmaybethelast.R

object ArtistData {
    val listData =
        List(10) {
            Artist(
                name = "HaoNhan$it",
                born = "2000",
                description = "HaoNhan$it",
                imageSource = R.drawable.db,
                genre = "HaoNhan$it",                country = "HaoNhan$it",
                music = MusicData.musicList
            )

        }
}