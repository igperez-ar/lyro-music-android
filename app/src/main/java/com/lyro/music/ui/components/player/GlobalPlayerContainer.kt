package com.lyro.music.ui.components.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lyro.music.ui.modules.home.HomeScreenPreview
import com.lyro.music.ui.modules.player.PlayerScreen
import com.lyro.music.ui.state.FakePlayerState
import com.lyro.music.ui.state.LocalPlayerNavigation
import com.lyro.music.ui.state.PlayerState

@Composable
fun GlobalPlayerContainer(
    playerState: PlayerState?,
    showPlayer: Boolean,
    onPrevPress: () -> Unit,
    onNextPress: () -> Unit,
    content: @Composable () -> Unit
) {
    val playerNavigation = LocalPlayerNavigation.current

    Box(modifier = Modifier.fillMaxSize()) {
        content()

        if (playerState != null && showPlayer) {
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = !playerNavigation.isPlayerExpanded,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                CompactPlayerView(
                    modifier = Modifier.fillMaxWidth(),
                    playerState = playerState,
                    onPlayerClick = { playerNavigation.expandPlayer() }
                )
            }

            AnimatedVisibility(
                visible = playerNavigation.isPlayerExpanded,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                PlayerScreen(
                    playerState = playerState,
                    onPrevPress = onPrevPress,
                    onNextPress = onNextPress,
                    onClose = { playerNavigation.collapsePlayer() }
                )
            }
        }
    }
}

@Preview
@Composable
fun GlobalPlayerContainerPreview() {
    GlobalPlayerContainer(
        playerState = FakePlayerState(),
        showPlayer = true,
        onPrevPress = {},
        onNextPress = {},
        content = { HomeScreenPreview() }
    )
}