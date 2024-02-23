package com.example.firstappandmaybethelast

import com.example.firstappandmaybethelast.realmdb.Artist
import com.example.firstappandmaybethelast.realmdb.Music
import com.example.firstappandmaybethelast.realmdb.MusicPlaylist
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object ext {
    val realm = Realm.open(RealmConfiguration.create(schema = setOf(Artist::class, Music::class, MusicPlaylist::class)))
    const val apiMusicResource: String = "https://ap-southeast-1.aws.data.mongodb-api.com/app/data-pkcss/endpoint/"
    const val apiFavoriteMusic: String = ""
    var listMusic: List<Music> = emptyList()
    fun getMusicData(): List<Music> = realm.query(Music::class).find().toList()

}