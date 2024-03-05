package com.example.firstappandmaybethelast.realmdb

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey

class Album: RealmObject {
    @PrimaryKey
    @Ignore
     var id: String = ""
     var title: String = ""
     var imageSource: String = ""
     var musicList: RealmList<String> = realmListOf()
}