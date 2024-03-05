package com.example.firstappandmaybethelast.realmdb

import android.os.Parcel
import android.os.Parcelable
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey

class Music() : RealmObject, Parcelable {
    @PrimaryKey
     var _id: String = ""
     var title: String = ""
     var artist: String = ""
     var genre: String = ""
     var musicSource: String = ""
     var imageResource: String = ""
     var isFavorite: Boolean = false
     var order: Int = 0

    constructor(parcel: Parcel) : this() {
        _id = parcel.readString().toString()
        title = parcel.readString().toString()
        artist = parcel.readString().toString()
        genre = parcel.readString().toString()
        musicSource = parcel.readString().toString()
        imageResource = parcel.readString().toString()
        order = parcel.readInt()
        isFavorite = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(genre)
        parcel.writeString(musicSource)
        parcel.writeString(imageResource)
        parcel.writeInt(order)
        parcel.writeByte(if (isFavorite) 1 else 0)
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

class FavoriteMusic():  RealmObject{
    @PrimaryKey
    @Ignore
    val id: String = ""
}