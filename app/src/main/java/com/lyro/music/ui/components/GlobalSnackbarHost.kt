package com.lyro.music.ui.components

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.lyro.music.ui.state.LocalSnackbarManager

@Composable
fun GlobalSnackbarHost() {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarManager = LocalSnackbarManager.current

    LaunchedEffect(Unit) {
        snackbarManager.snackbarMessages.collect { message ->
            val result = snackbarHostState.showSnackbar(
                message = message.message,
                actionLabel = message.actionLabel,
                withDismissAction = message.withDismissAction,
                duration = message.duration
            )
            if (result == SnackbarResult.ActionPerformed) {
                message.onAction?.invoke()
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState)
} 