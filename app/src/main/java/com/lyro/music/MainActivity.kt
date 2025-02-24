package com.lyro.music

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lyro.music.core.capabilities.songs.domain.serializer.toMediaItem
import com.lyro.music.extension.getReadStoragePermission
import com.lyro.music.extension.playMediaAt
import com.lyro.music.extension.updatePlaylist
import com.lyro.music.ui.components.GlobalSnackbarHost
import com.lyro.music.ui.components.player.GlobalPlayerContainer
import com.lyro.music.ui.components.rememberManagedMediaController
import com.lyro.music.ui.modules.home.HomeScreen
import com.lyro.music.ui.modules.home.HomeViewModel
import com.lyro.music.ui.state.LocalPlayerNavigation
import com.lyro.music.ui.state.LocalSnackbarManager
import com.lyro.music.ui.state.PlayerNavigationState
import com.lyro.music.ui.state.PlayerViewModel
import com.lyro.music.ui.state.SnackbarManager
import com.lyro.music.ui.state.state
import com.lyro.music.ui.theme.LyroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LyroTheme {
                val playerViewModel = viewModel<PlayerViewModel>()
                val mediaController by rememberManagedMediaController()
                val playerState by playerViewModel.playerState.collectAsStateWithLifecycle()
                val isPlayerSetUp by playerViewModel.isPlayerSetUp.collectAsStateWithLifecycle()
                val snackbarManager = remember { SnackbarManager() }

                val viewModel = viewModel<HomeViewModel>()
                val songs by viewModel.songs.collectAsStateWithLifecycle()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted -> if (isGranted) viewModel.loadSongs() }
                )

                LaunchedEffect(Unit) {
                    launcher.launch(getReadStoragePermission())
                }

                LaunchedEffect(songs) {
                    if (songs.isNotEmpty()) {
                        mediaController?.updatePlaylist(songs.map { item -> item.toMediaItem() })
                    }
                }

                DisposableEffect(mediaController) {
                    mediaController?.run {
                        playerViewModel.updatePlayerState(state())
                    }
                    onDispose {
                        playerViewModel.updatePlayerState(null)
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    when(ContextCompat.checkSelfPermission(
                        this@MainActivity, getReadStoragePermission()
                    )) {
                        PackageManager.PERMISSION_DENIED ->
                            PermissionsRationale(onGrantClick = {
                                launcher.launch(getReadStoragePermission())
                            })
                        PackageManager.PERMISSION_GRANTED -> CompositionLocalProvider(
                            LocalPlayerNavigation provides remember { PlayerNavigationState() },
                            LocalSnackbarManager provides snackbarManager
                        ) {
                            Scaffold(snackbarHost = { GlobalSnackbarHost() }) { paddingValues ->
                                Box(modifier = Modifier.padding(paddingValues)) {
                                    GlobalPlayerContainer(
                                        playerState = playerState,
                                        onPrevPress = { mediaController?.seekToPreviousMediaItem() },
                                        onNextPress = { mediaController?.seekToNextMediaItem() },
                                        showPlayer = isPlayerSetUp,
                                    ) {
                                        HomeScreen(songs = songs, onPlayItem = { index ->
                                            playerViewModel.setupPlayer()
                                            mediaController?.playMediaAt(index)
                                        })
                                    }
                                }
                            }
                        }
                    }
                }

                // Handle player errors globally
                LaunchedEffect(Unit) {
                    playerViewModel.errorEvent.collect { error ->
                        snackbarManager.showMessage(message = "${error.message}, Code: ${error.errorCode}",
                            actionLabel = "Retry",
                            withDismissAction = true,
                            onAction = { mediaController?.prepare() })
                    }
                }
            }
        }
    }

    @Composable
    private fun PermissionsRationale(
        modifier: Modifier = Modifier,
        onGrantClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.8F),
                text = stringResource(id = R.string.read_permissions_rationale),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onGrantClick) {
                Text(text = stringResource(id = R.string.grant_permissions))
            }
        }
    }
}