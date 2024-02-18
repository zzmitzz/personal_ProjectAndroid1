package com.example.firstappandmaybethelast.realmdb

import android.os.Parcel
import android.os.Parcelable
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.bson.types.ObjectId

class Music() : RealmObject, Parcelable {
    @PrimaryKey
    @Ignore
     var id: ObjectId = ObjectId()
     var title: String = ""
     var artist: String = ""
     var genre: String = ""
     var musicSource: String = ""
     var imageResource: String = ""


    constructor(parcel: Parcel) : this() {
        title = parcel.readString().toString()
        artist = parcel.readString().toString()
        genre = parcel.readString().toString()
        musicSource = parcel.readString().toString()
        imageResource = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(genre)
        parcel.writeString(musicSource)
        parcel.writeString(imageResource)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }
}