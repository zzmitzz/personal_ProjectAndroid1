package com.example.firstappandmaybethelast

import com.example.firstappandmaybethelast.realmdb.Album
import com.example.firstappandmaybethelast.realmdb.Artist
import com.example.firstappandmaybethelast.realmdb.FavoriteMusic
import com.example.firstappandmaybethelast.realmdb.Music
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object ext {
    val realm = Realm.open(RealmConfiguration.create(schema = setOf(Artist::class, Music::class, Album::class, FavoriteMusic::class)))
    const val apiMusicResource: String = "https://ap-southeast-1.aws.data.mongodb-api.com/app/data-pkcss/endpoint/"
    var listMusic: List<Music> = emptyList()
    var customListMusic: List<Music> = emptyList()
    var customAlbumList: MutableList<Album> = mutableListOf<Album>()
    fun getMusicData(): List<Music> = realm.query(Music::class).find().toList()
    fun getAlbumData(): List<Album> = realm.query(Album::class).find().toList()
}