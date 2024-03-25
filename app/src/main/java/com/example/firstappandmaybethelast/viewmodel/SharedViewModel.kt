package com.example.firstappandmaybethelast.viewmodel

import androidx.lifecycle.ViewModel
import com.example.firstappandmaybethelast.realmdb.Music

class SharedViewModel: ViewModel() {
    var listMusic: List<Music> = emptyList()


}