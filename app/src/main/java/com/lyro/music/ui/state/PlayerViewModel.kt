package com.lyro.music.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.PlaybackException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _playerState = MutableStateFlow<PlayerState?>(null)
    val playerState: StateFlow<PlayerState?> = _playerState.asStateFlow()

    private val _isPlayerSetUp = MutableStateFlow(false)
    val isPlayerSetUp: StateFlow<Boolean> = _isPlayerSetUp

    fun setupPlayer() {
        _isPlayerSetUp.value = true
    }

    private val _errorEvent = MutableSharedFlow<PlaybackException>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun updatePlayerState(newState: PlayerState?) {
        _playerState.value = newState
        // Handle error state changes
        newState?.playerError?.let { error ->
            viewModelScope.launch {
                _errorEvent.emit(error)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _playerState.value?.dispose()
    }
} 