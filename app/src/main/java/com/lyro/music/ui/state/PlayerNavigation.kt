package com.lyro.music.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class PlayerNavigationState {
    var isPlayerExpanded by mutableStateOf(false)
        private set

    fun expandPlayer() {
        isPlayerExpanded = true
    }

    fun collapsePlayer() {
        isPlayerExpanded = false
    }
}

val LocalPlayerNavigation = staticCompositionLocalOf { PlayerNavigationState() }