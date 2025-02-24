package com.lyro.music.ui.components.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyro.music.ui.components.SongThumbnail
import com.lyro.music.ui.state.PlayerState
import com.lyro.music.extension.isBuffering

@Composable
fun CompactPlayerView(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onPlayerClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { onPlayerClick() },
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentMediaItem = playerState.currentMediaItem
                if (currentMediaItem != null) {
                    SongThumbnail(
                        albumArtUri = currentMediaItem.mediaMetadata.artworkUri!!,
                        contentDescription = null
                    )
                    Column {
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp),
                            text = currentMediaItem.mediaMetadata.displayTitle.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp),
                            text = currentMediaItem.mediaMetadata.artist.toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            PlayPauseButton(
                modifier = Modifier
                    .size(40.dp),
                isPlaying = playerState.isPlaying,
                isBuffering = playerState.isBuffering
            ) {
                with(playerState.player) {
                    playWhenReady = !playWhenReady
                }
            }

        }
    }
}