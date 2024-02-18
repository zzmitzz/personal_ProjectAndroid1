package com.example.firstappandmaybethelast.realmdb

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.bson.types.ObjectId

class Artist: RealmObject {
    @PrimaryKey
    @Ignore
     var id: ObjectId = ObjectId()
     var name: String = ""
     var born: String = ""
     var description: String = ""
     var imageSource: String = ""
     var genre: String = ""
     var country: String = ""
     var music: RealmList<Music> = realmListOf()
}