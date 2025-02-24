package com.lyro.music.ui.modules.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.lyro.music.ui.components.player.MiniPlayerArtworkView
import com.lyro.music.ui.components.player.NextButton
import com.lyro.music.ui.components.player.PlayPauseButton
import com.lyro.music.ui.components.player.PreviousButton
import com.lyro.music.ui.components.player.TimeBar
import com.lyro.music.ui.state.FakePlayerState
import com.lyro.music.ui.state.PlayerState
import com.lyro.music.extension.isBuffering

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    playerState: PlayerState,
    onPrevPress: () -> Unit,
    onNextPress: () -> Unit,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Now Playing") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Close player")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val currentMediaItem = playerState.currentMediaItem
            if (currentMediaItem != null) {
                MiniPlayerArtworkView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp),
                    artworkUri = currentMediaItem.mediaMetadata.artworkUri
                )
                Text(
                    text = currentMediaItem.mediaMetadata.displayTitle.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = currentMediaItem.mediaMetadata.artist?.toString() ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
                TimeBar(modifier = Modifier.padding(vertical = 32.dp), player = playerState.player)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PreviousButton(onClick = onPrevPress)
                    PlayPauseButton(
                        modifier = Modifier.size(64.dp),
                        isPlaying = playerState.isPlaying,
                        isBuffering = playerState.isBuffering
                    ) {
                        with(playerState.player) {
                            playWhenReady = !playWhenReady
                        }
                    }
                    NextButton(onClick = onNextPress)
                }
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Preview
@Composable
fun PlayerScreenPreview() {
    PlayerScreen(
        playerState = FakePlayerState(),
        onPrevPress = {},
        onNextPress = {},
        onClose = {}
    )
}