package com.lyro.music.core.capabilities.songs.domain.serializer

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.lyro.music.core.capabilities.songs.domain.dtos.Song

fun Song.toMediaItem(): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setDisplayTitle(title)
        .setArtworkUri(imagePath)
        .setArtist(artist)
        .build()
    return MediaItem.Builder()
        .setUri(data)
        .setMediaId(id)
        .setMediaMetadata(metadata)
        .build()
}