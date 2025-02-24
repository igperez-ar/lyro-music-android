package com.lyro.music.ui.state

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class SnackbarMessage(
    val message: String,
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val withDismissAction: Boolean = false,
    val onAction: (() -> Unit)? = null
)

class SnackbarManager {
    private val _snackbarMessages = MutableSharedFlow<SnackbarMessage>()
    val snackbarMessages = _snackbarMessages.asSharedFlow()

    suspend fun showMessage(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        withDismissAction: Boolean = false,
        onAction: (() -> Unit)? = null
    ) {
        _snackbarMessages.emit(
            SnackbarMessage(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
                withDismissAction = withDismissAction,
                onAction = onAction
            )
        )
    }
}

val LocalSnackbarManager = staticCompositionLocalOf { SnackbarManager() } 