package com.example.firstappandmaybethelast

import com.example.firstappandmaybethelast.realmdb.Artist
import com.example.firstappandmaybethelast.realmdb.Music
import com.example.firstappandmaybethelast.realmdb.MusicPlaylist
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object ext {
    val realm = Realm.open(RealmConfiguration.create(schema = setOf(Artist::class, Music::class, MusicPlaylist::class)))
}