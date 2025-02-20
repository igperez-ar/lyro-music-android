package com.lyro.music.core.capabilities.songs.repository

import com.lyro.music.core.capabilities.songs.domain.dtos.Song

interface SongsRepository {
    fun getAllSongs(): List<Song>
} 