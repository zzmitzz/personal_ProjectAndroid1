package com.example.firstappandmaybethelast.model

import com.example.firstappandmaybethelast.musicdata.Music

sealed interface ApiState {
    data object Loading : ApiState
    data class Success(val music: Music) : ApiState
    data class Error(val throwable: Throwable) : ApiState
}