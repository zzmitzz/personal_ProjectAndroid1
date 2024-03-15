package com.example.firstappandmaybethelast.realmdb

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey

class Artist: RealmObject {
    @PrimaryKey
    @Ignore
    var id: String = ""
    var name: String = ""
    var image: String = ""
    var music: RealmList<String> = realmListOf()
}