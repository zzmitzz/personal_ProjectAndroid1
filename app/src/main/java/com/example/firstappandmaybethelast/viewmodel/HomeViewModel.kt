package com.example.firstappandmaybethelast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.musicdata.Music
import com.example.firstappandmaybethelast.musicdata.MusicData

class HomeViewModel: ViewModel() {
    private var _musicList: List<Music> = MusicData.musicList
    val musicList: List<Music>
        get() = _musicList
    private val _musicAdapter = MusicAdapter()
    val musicAdapter: MusicAdapter
        get() = _musicAdapter
    init {
        _musicAdapter.setMusic(_musicList)
    }
}