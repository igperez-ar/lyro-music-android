package com.lyro.music.ui.modules.home

import androidx.lifecycle.ViewModel
import com.lyro.music.core.capabilities.songs.domain.dtos.Song
import com.lyro.music.core.capabilities.songs.domain.usecases.GetSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase
) : ViewModel() {
    private val _isPlayerSetUp = MutableStateFlow(false)
    val isPlayerSetUp: StateFlow<Boolean> = _isPlayerSetUp

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    init {
        loadSongs()
    }

    private fun loadSongs() {
        _songs.value = getSongsUseCase()
    }

    fun setupPlayer() {
        _isPlayerSetUp.value = true
    }
}