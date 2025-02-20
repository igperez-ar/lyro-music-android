package com.lyro.music.core.capabilities.songs.infrastructure.datasource

import android.net.Uri
import com.lyro.music.core.capabilities.songs.domain.dtos.Song
import com.lyro.music.core.capabilities.songs.repository.SongsRepository
import javax.inject.Inject

class MockSongsDataSource @Inject constructor() : SongsRepository {
    override fun getAllSongs(): List<Song> = listOf(
        Song(
            id = "22",
            title = "Bring Me To Life",
            artist = "Evanescence",
            album = "Fallen",
            imagePath = Uri.parse("content://media/external/audio/albumart/2874160801125994969"),
            duration = 240288,
            data = Uri.parse("/storage/emulated/0/Download/Bring Me To Life.mp3")
        ),
        Song(
            id = "23",
            title = "The Sound of Silence",
            artist = "Disturbed",
            album = "The Sound of Silence",
            imagePath = Uri.parse("content://media/external/audio/albumart/8176272775969190961"),
            duration = 229648,
            data = Uri.parse("/storage/emulated/0/Download/The Sound of Silence.mp3")
        ),
        Song(
            id = "21",
            title = "Divenire",
            artist = "Ludovico Einaudi",
            album = "In a Time Lapse",
            imagePath = Uri.parse("content://media/external/audio/albumart/1217913730238972260"),
            duration = 248743,
            data = Uri.parse("/storage/emulated/0/Download/Divenire.mp3")
        ),
    )
} 