package com.example.firstappandmaybethelast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.adapter.MusicListAdapter
import com.example.firstappandmaybethelast.ext
import com.example.firstappandmaybethelast.musicdata.MusicPlaylist
import com.example.firstappandmaybethelast.musicdata.MusicPlaylistData
import com.example.firstappandmaybethelast.realmdb.Music

class HomeViewModel: ViewModel() {
    private var _musicList: List<Music> = ext.getMusicData()
    private var _musicPlaylistList: List<MusicPlaylist> = MusicPlaylistData.listData
    private val _musicAdapter = MusicAdapter()
    private val _musicPlaylistAdapter = MusicListAdapter()
    val musicPlaylistAdapter : MusicListAdapter
        get() = _musicPlaylistAdapter
    val musicAdapter: MusicAdapter
        get() = _musicAdapter
    init {
        ext.listMusic = _musicList
        _musicAdapter.setMusic(ext.listMusic)
        _musicPlaylistAdapter.setMusic(_musicPlaylistList)
    }
}