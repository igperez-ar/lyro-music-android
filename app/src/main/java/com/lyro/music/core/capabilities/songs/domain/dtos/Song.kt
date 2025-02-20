package com.lyro.music.core.capabilities.songs.domain.dtos

import android.net.Uri

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val imagePath: Uri,
    val duration: Long,
    val data: Uri
)
