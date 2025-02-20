package com.lyro.music.core.capabilities.songs.domain.usecases

import com.lyro.music.core.capabilities.songs.domain.dtos.Song
import com.lyro.music.core.capabilities.songs.repository.SongsRepository
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(
    private val songsRepository: SongsRepository
) {
    operator fun invoke(): List<Song> {
        return songsRepository.getAllSongs()
    }
} 