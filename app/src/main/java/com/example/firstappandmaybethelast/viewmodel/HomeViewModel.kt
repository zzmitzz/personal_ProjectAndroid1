package com.example.firstappandmaybethelast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.adapter.MusicAdapter
import com.example.firstappandmaybethelast.adapter.MusicListAdapter
import com.example.firstappandmaybethelast.ext

class HomeViewModel: ViewModel() {
    private val _musicAdapter = MusicAdapter()
    private val _musicPlaylistAdapter = MusicListAdapter()
    val musicPlaylistAdapter : MusicListAdapter
        get() = _musicPlaylistAdapter
    val musicAdapter: MusicAdapter
        get() = _musicAdapter
    init {
        ext.customListMusic = ext.listMusic
        ext.customAlbumList = ext.getAlbumData().toMutableList()
        _musicPlaylistAdapter.setList(ext.customAlbumList)
        _musicAdapter.setMusic(ext.customListMusic)
    }
}