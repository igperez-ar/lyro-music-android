package com.lyro.music.ui.modules.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lyro.music.core.capabilities.songs.domain.serializer.toMediaItem
import com.lyro.music.ui.components.SongList
import com.lyro.music.ui.components.player.CompactPlayerView
import com.lyro.music.ui.components.rememberManagedMediaController
import com.lyro.music.ui.state.PlayerState
import com.lyro.music.ui.state.state
import com.lyro.music.ui.utility.playMediaAt
import com.lyro.music.ui.utility.updatePlaylist


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val songs by viewModel.songs.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .statusBarsPadding(),
        topBar = {
            TopAppBar(title = { Text(text = "Lyro") })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        content = { paddingValues ->
            val isPlayerSetUp by viewModel.isPlayerSetUp.collectAsStateWithLifecycle()
            val mediaController by rememberManagedMediaController()

            LaunchedEffect(key1 = isPlayerSetUp) {
                if (isPlayerSetUp) {
                    mediaController?.run {
                        if (mediaItemCount > 0) {
                            prepare()
                            play()
                        }
                    }
                }
            }

            var playerState: PlayerState? by remember {
                mutableStateOf(mediaController?.state())
            }

            DisposableEffect(key1 = mediaController) {
                mediaController?.run {
                    playerState = state()
                }
                onDispose {
                    playerState?.dispose()
                }
            }

            LaunchedEffect(key1 = playerState?.playerError) {
                playerState?.playerError?.let { exception ->
                    val result = snackbarHostState.showSnackbar(
                        message = "${exception.message}, Code: ${exception.errorCode}",
                        withDismissAction = true,
                        actionLabel = "Retry"
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        mediaController?.prepare()
                    }
                }
            }
            mediaController?.updatePlaylist(songs.map { item -> item.toMediaItem() })
            ConstraintLayout(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                SongList(songs = songs, onItemClick = { index ->
                    viewModel.setupPlayer()
                    mediaController?.playMediaAt(index)
                })
            }
            // Remember a CoroutineScope for launching coroutines
            val coroutineScope = rememberCoroutineScope()
            // Show the compact player view when the player is set up
            if (isPlayerSetUp && playerState != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CompactPlayerView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .clickable {},
                        playerState = playerState!!
                    )
                }
            }
        }
    )
}