package com.example.firstappandmaybethelast.realmdb

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.bson.types.ObjectId

class MusicPlaylist: RealmObject {
    @PrimaryKey
    @Ignore
     var id: ObjectId = ObjectId()
     var title: String = ""
    var duration: Long = 0
     var imageSource: String = ""
     var musicList: RealmList<Music> = realmListOf()
}