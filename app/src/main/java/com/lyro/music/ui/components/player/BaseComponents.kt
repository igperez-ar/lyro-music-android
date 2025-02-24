package com.lyro.music.ui.components.player

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.ui.R
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun PlayPauseButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    isBuffering: Boolean,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onTogglePlayPause: () -> Unit
) {
    if (isBuffering) {
        PlayerProgressIndicator(
            modifier = modifier,
            progressTint = iconTint
        )
    }
    else {
        IconButton(
            modifier = modifier,
            onClick = {
                onTogglePlayPause()
            }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(fraction = .8f),
                painter = painterResource(
                    id = if (isPlaying)
                        R.drawable.exo_icon_pause
                    else
                        R.drawable.exo_icon_play
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = iconTint
            )
        }
    }
}

@Composable
fun PlayerProgressIndicator(
    modifier: Modifier = Modifier,
    progressTint: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(.8f)
                .align(Center),
            color = progressTint,
            trackColor = Color.Transparent
        )
    }
}

@Composable
fun MiniPlayerArtworkView(
    modifier: Modifier = Modifier,
    artworkUri: Uri?
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = artworkUri,
            contentDescription = null,
        )
    }
}

@Composable
fun TimeBar(
    modifier: Modifier = Modifier,
    player: Player
) {
    val currentPosition = remember { mutableLongStateOf(0L) }
    val duration = remember { mutableLongStateOf(0L) }
    val isTracking = remember { mutableStateOf(false) }

    // Generate random heights for bars (in a real app, this would be actual waveform data)
    val barHeights = remember {
        List(100) { Random.nextFloat() * 0.8f + 0.2f }
    }

    LaunchedEffect(player) {
        while (true) {
            if (!isTracking.value) {
                currentPosition.longValue = player.currentPosition
            }
            delay(16)
        }
    }

    LaunchedEffect(player) {
        snapshotFlow { player.duration }
            .collect { 
                duration.longValue = it
                if (!isTracking.value) {
                    currentPosition.longValue = 0L
                }
            }
    }

    val durationValue = duration.longValue
    if (durationValue > 0) {
        WaveformProgressBar(
            modifier = modifier
                .height(48.dp)
                .fillMaxWidth(),
            progress = currentPosition.longValue.toFloat() / durationValue.toFloat(),
            barHeights = barHeights,
            onProgressChange = { progress ->
                isTracking.value = true
                currentPosition.longValue = (progress * durationValue).toLong()
            },
            onProgressChangeFinished = {
                player.seekTo(currentPosition.longValue)
                isTracking.value = false
            }
        )
    }
}

@Composable
private fun WaveformProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    barHeights: List<Float>,
    onProgressChange: (Float) -> Unit,
    onProgressChangeFinished: () -> Unit
) {
    val color = MaterialTheme.colorScheme.primary;
    var isInteracting by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        isInteracting = true
                        val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                        onProgressChange(newProgress)
                        onProgressChangeFinished()
                        isInteracting = false
                    }
                )
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { isInteracting = true },
                    onDragEnd = {
                        isInteracting = false
                        onProgressChangeFinished()
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        val delta = dragAmount / size.width
                        onProgressChange((progress + delta).coerceIn(0f, 1f))
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barWidth = (size.width / barHeights.size) * 0.8f
            val spacing = (size.width / barHeights.size) * 1f
            val progressWidth = size.width * progress

            barHeights.forEachIndexed { index, height ->
                val barX = index * (barWidth + spacing)
                val barHeight = size.height * height

                drawRect(
                    color = if (barX <= progressWidth)
                        color
                    else
                        color.copy(alpha = 0.3f),
                    topLeft = Offset(
                        x = barX,
                        y = (size.height - barHeight) / 2
                    ),
                    size = Size(barWidth, barHeight)
                )
            }
        }
    }
}

@Composable
fun PreviousButton(
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(fraction = .8f),
            painter = painterResource(R.drawable.exo_icon_previous),
            contentDescription = "Previous",
            tint = iconTint
        )
    }
}

@Composable
fun NextButton(
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(fraction = .8f),
            painter = painterResource(R.drawable.exo_icon_next),
            contentDescription = "Previous",
            tint = iconTint
        )
    }
}