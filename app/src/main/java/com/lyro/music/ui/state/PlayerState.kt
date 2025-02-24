package com.lyro.music.ui.state

import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.AudioAttributes
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.Size
import androidx.media3.common.util.UnstableApi
import com.lyro.music.core.capabilities.songs.domain.serializer.toMediaItem
import com.lyro.music.core.capabilities.songs.infrastructure.datasource.MockSongsDataSource

/**
 * Create a instance of [PlayerState] and register a [listener][Player.Listener] to the [Player] to
 * observe its states.
 *
 * NOTE: Should call [dispose][PlayerState.dispose] to unregister the listener to avoid leaking this
 * instance when it is no longer used.
 */
fun Player.state(): PlayerState {
    return PlayerStateImpl(this)
}

/**
 * A state object that can be used to observe the [player]'s states.
 */
interface PlayerState {
    val player: Player

    val timeline: Timeline

    val mediaItemIndex: Int

    val tracks: Tracks

    val currentMediaItem: MediaItem?

    val mediaMetadata: MediaMetadata

    val playlistMetadata: MediaMetadata

    val isLoading: Boolean

    val availableCommands: Player.Commands

    val trackSelectionParameters: TrackSelectionParameters

    @get:Player.State
    val playbackState: Int

    val playWhenReady: Boolean

    @get:Player.PlaybackSuppressionReason
    val playbackSuppressionReason: Int

    val isPlaying: Boolean

    @get:Player.RepeatMode
    val repeatMode: Int

    val shuffleModeEnabled: Boolean

    val playerError: PlaybackException?

    val playbackParameters: PlaybackParameters

    val seekBackIncrement: Long

    val seekForwardIncrement: Long

    val maxSeekToPreviousPosition: Long

    val audioAttributes: AudioAttributes

    val volume: Float

    val deviceInfo: DeviceInfo

    val deviceVolume: Int

    val isDeviceMuted: Boolean

    val videoSize: VideoSize

    val cues: CueGroup

    fun dispose()
}

internal class PlayerStateImpl(
    override val player: Player
) : PlayerState {
    override var timeline: Timeline by mutableStateOf(player.currentTimeline)
        private set

    override var mediaItemIndex: Int by mutableIntStateOf(player.currentMediaItemIndex)
        private set

    override var tracks: Tracks by mutableStateOf(player.currentTracks)
        private set

    override var currentMediaItem: MediaItem? by mutableStateOf(player.currentMediaItem)
        private set

    override var mediaMetadata: MediaMetadata by mutableStateOf(player.mediaMetadata)
        private set

    override var playlistMetadata: MediaMetadata by mutableStateOf(player.playlistMetadata)
        private set

    override var isLoading: Boolean by mutableStateOf(player.isLoading)
        private set

    override var availableCommands: Player.Commands by mutableStateOf(player.availableCommands)
        private set

    override var trackSelectionParameters: TrackSelectionParameters by mutableStateOf(player.trackSelectionParameters)
        private set

    @get:Player.State
    override var playbackState: Int by mutableIntStateOf(player.playbackState)
        private set

    override var playWhenReady: Boolean by mutableStateOf(player.playWhenReady)
        private set

    @get:Player.PlaybackSuppressionReason
    override var playbackSuppressionReason: Int by mutableIntStateOf(player.playbackSuppressionReason)
        private set

    override var isPlaying: Boolean by mutableStateOf(player.isPlaying)
        private set

    @get:Player.RepeatMode
    override var repeatMode: Int by mutableIntStateOf(player.repeatMode)
        private set

    override var shuffleModeEnabled: Boolean by mutableStateOf(player.shuffleModeEnabled)
        private set

    override var playerError: PlaybackException? by mutableStateOf(player.playerError)
        private set

    override var playbackParameters: PlaybackParameters by mutableStateOf(player.playbackParameters)
        private set

    override var seekBackIncrement: Long by mutableLongStateOf(player.seekBackIncrement)
        private set

    override var seekForwardIncrement: Long by mutableLongStateOf(player.seekForwardIncrement)
        private set

    override var maxSeekToPreviousPosition: Long by mutableLongStateOf(player.maxSeekToPreviousPosition)
        private set

    override var audioAttributes: AudioAttributes by mutableStateOf(player.audioAttributes)
        private set

    override var volume: Float by mutableFloatStateOf(player.volume)
        private set

    override var deviceInfo: DeviceInfo by mutableStateOf(player.deviceInfo)
        private set

    override var deviceVolume: Int by mutableIntStateOf(player.deviceVolume)
        private set

    override var isDeviceMuted: Boolean by mutableStateOf(player.isDeviceMuted)
        private set

    override var videoSize: VideoSize by mutableStateOf(player.videoSize)
        private set

    override var cues: CueGroup by mutableStateOf(player.currentCues)
        private set

    private val listener = object : Player.Listener {
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            this@PlayerStateImpl.timeline = timeline
            this@PlayerStateImpl.mediaItemIndex = player.currentMediaItemIndex
        }

        override fun onTracksChanged(tracks: Tracks) {
            this@PlayerStateImpl.tracks = tracks
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            this@PlayerStateImpl.currentMediaItem = mediaItem
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            this@PlayerStateImpl.mediaMetadata = mediaMetadata
        }

        override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {
            this@PlayerStateImpl.playlistMetadata = mediaMetadata
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            this@PlayerStateImpl.isLoading = isLoading
        }

        override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
            this@PlayerStateImpl.availableCommands = availableCommands
        }

        override fun onTrackSelectionParametersChanged(parameters: TrackSelectionParameters) {
            this@PlayerStateImpl.trackSelectionParameters = parameters
        }

        override fun onPlaybackStateChanged(@Player.State playbackState: Int) {
            this@PlayerStateImpl.playbackState = playbackState
        }

        override fun onPlayWhenReadyChanged(
            playWhenReady: Boolean,
            @Player.PlayWhenReadyChangeReason reason: Int
        ) {
            this@PlayerStateImpl.playWhenReady = playWhenReady
        }

        override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {
            this@PlayerStateImpl.playbackSuppressionReason = playbackSuppressionReason
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            this@PlayerStateImpl.isPlaying = isPlaying
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            this@PlayerStateImpl.repeatMode = repeatMode
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            this@PlayerStateImpl.shuffleModeEnabled = shuffleModeEnabled
        }

        override fun onPlayerErrorChanged(error: PlaybackException?) {
            this@PlayerStateImpl.playerError = error
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            this@PlayerStateImpl.mediaItemIndex = player.currentMediaItemIndex
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            this@PlayerStateImpl.playbackParameters = playbackParameters
        }

        override fun onSeekBackIncrementChanged(seekBackIncrementMs: Long) {
            this@PlayerStateImpl.seekBackIncrement = seekBackIncrementMs
        }

        override fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {
            this@PlayerStateImpl.seekForwardIncrement = seekForwardIncrementMs
        }

        override fun onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs: Long) {
            this@PlayerStateImpl.maxSeekToPreviousPosition = maxSeekToPreviousPositionMs
        }

        override fun onAudioAttributesChanged(audioAttributes: AudioAttributes) {
            this@PlayerStateImpl.audioAttributes = audioAttributes
        }

        override fun onVolumeChanged(volume: Float) {
            this@PlayerStateImpl.volume = volume
        }

        override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
            this@PlayerStateImpl.deviceInfo = deviceInfo
        }

        override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
            this@PlayerStateImpl.deviceVolume = volume
            this@PlayerStateImpl.isDeviceMuted = muted
        }

        override fun onVideoSizeChanged(videoSize: VideoSize) {
            this@PlayerStateImpl.videoSize = videoSize
        }

        override fun onCues(cues: CueGroup) {
            this@PlayerStateImpl.cues = cues
        }
    }

    init {
        player.addListener(listener)
    }

    override fun dispose() {
        player.removeListener(listener)
    }
}

@UnstableApi
class FakePlayer : Player {
    override fun getApplicationLooper(): Looper = Looper.getMainLooper()
    override fun addListener(listener: Player.Listener) {}
    override fun removeListener(listener: Player.Listener) {}
    override fun setMediaItems(mediaItems: MutableList<MediaItem>) {}
    override fun setMediaItems(mediaItems: MutableList<MediaItem>, resetPosition: Boolean) {}
    override fun setMediaItems(
        mediaItems: MutableList<MediaItem>,
        startIndex: Int,
        startPositionMs: Long
    ) {
    }

    override fun setMediaItem(mediaItem: MediaItem) {}
    override fun setMediaItem(mediaItem: MediaItem, startPositionMs: Long) {}
    override fun setMediaItem(mediaItem: MediaItem, resetPosition: Boolean) {}
    override fun addMediaItem(mediaItem: MediaItem) {}
    override fun addMediaItem(index: Int, mediaItem: MediaItem) {}
    override fun addMediaItems(mediaItems: MutableList<MediaItem>) {}
    override fun addMediaItems(index: Int, mediaItems: MutableList<MediaItem>) {}
    override fun moveMediaItem(currentIndex: Int, newIndex: Int) {}
    override fun moveMediaItems(fromIndex: Int, toIndex: Int, newIndex: Int) {}
    override fun replaceMediaItem(index: Int, mediaItem: MediaItem) {}
    override fun replaceMediaItems(
        fromIndex: Int,
        toIndex: Int,
        mediaItems: MutableList<MediaItem>
    ) {
    }

    override fun removeMediaItem(index: Int) {}
    override fun removeMediaItems(fromIndex: Int, toIndex: Int) {}
    override fun clearMediaItems() {}
    override fun isCommandAvailable(command: Int): Boolean = false
    override fun canAdvertiseSession(): Boolean = false
    override fun getAvailableCommands(): Player.Commands = Player.Commands.EMPTY
    override fun prepare() {}
    override fun getPlaybackState(): Int = Player.STATE_IDLE
    override fun getPlaybackSuppressionReason(): Int = Player.PLAYBACK_SUPPRESSION_REASON_NONE
    override fun isPlaying(): Boolean = false
    override fun getPlayerError(): PlaybackException? = null
    override fun play() {}
    override fun pause() {}
    override fun setPlayWhenReady(playWhenReady: Boolean) {}
    override fun getPlayWhenReady(): Boolean = true
    override fun setRepeatMode(repeatMode: Int) {}
    override fun getRepeatMode(): Int = Player.REPEAT_MODE_OFF
    override fun setShuffleModeEnabled(shuffleModeEnabled: Boolean) {}
    override fun getShuffleModeEnabled(): Boolean = false
    override fun isLoading(): Boolean = false
    override fun seekToDefaultPosition() {}
    override fun seekToDefaultPosition(mediaItemIndex: Int) {}
    override fun seekTo(positionMs: Long) {}
    override fun seekTo(mediaItemIndex: Int, positionMs: Long) {}
    override fun getSeekBackIncrement(): Long = 10L
    override fun seekBack() {}
    override fun getSeekForwardIncrement(): Long = 10L
    override fun seekForward() {}
    override fun hasPreviousMediaItem(): Boolean = false
    override fun seekToPreviousWindow() {}
    override fun seekToPreviousMediaItem() {}
    override fun getMaxSeekToPreviousPosition(): Long = 10L
    override fun seekToPrevious() {}
    override fun hasNext(): Boolean = true
    override fun hasNextWindow(): Boolean = true
    override fun hasNextMediaItem(): Boolean = true
    override fun next() {}
    override fun seekToNextWindow() {}
    override fun seekToNextMediaItem() {}
    override fun seekToNext() {}
    override fun setPlaybackParameters(playbackParameters: PlaybackParameters) {}
    override fun setPlaybackSpeed(speed: Float) {}
    override fun getPlaybackParameters(): PlaybackParameters = PlaybackParameters.DEFAULT
    override fun stop() {}
    override fun release() {}
    override fun getCurrentTracks(): Tracks = Tracks.EMPTY
    override fun getTrackSelectionParameters(): TrackSelectionParameters =
        TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT

    override fun setTrackSelectionParameters(parameters: TrackSelectionParameters) {}
    override fun getMediaMetadata(): MediaMetadata = MediaMetadata.EMPTY
    override fun getPlaylistMetadata(): MediaMetadata = MediaMetadata.EMPTY
    override fun setPlaylistMetadata(mediaMetadata: MediaMetadata) {}
    override fun getCurrentManifest(): Any? = null
    override fun getCurrentTimeline(): Timeline = Timeline.EMPTY
    override fun getCurrentPeriodIndex(): Int = 0
    override fun getCurrentWindowIndex(): Int = 0
    override fun getCurrentMediaItemIndex(): Int = 0
    override fun getNextWindowIndex(): Int = 0
    override fun getNextMediaItemIndex(): Int = 0
    override fun getPreviousWindowIndex(): Int = 0
    override fun getPreviousMediaItemIndex(): Int = 0
    override fun getCurrentMediaItem(): MediaItem? = null
    override fun getMediaItemCount(): Int = 3
    override fun getMediaItemAt(index: Int): MediaItem = MediaItem.EMPTY
    override fun getDuration(): Long = 500L
    override fun getCurrentPosition(): Long = 0L
    override fun getBufferedPosition(): Long = 0L
    override fun getBufferedPercentage(): Int = 1
    override fun getTotalBufferedDuration(): Long = 1000L
    override fun isCurrentWindowDynamic(): Boolean = true
    override fun isCurrentMediaItemDynamic(): Boolean = true
    override fun isCurrentWindowLive(): Boolean = true
    override fun isCurrentMediaItemLive(): Boolean = true
    override fun getCurrentLiveOffset(): Long = 0L
    override fun isCurrentWindowSeekable(): Boolean = true
    override fun isCurrentMediaItemSeekable(): Boolean = true
    override fun isPlayingAd(): Boolean = false
    override fun getCurrentAdGroupIndex(): Int = 0
    override fun getCurrentAdIndexInAdGroup(): Int = 0
    override fun getContentDuration(): Long = 1000L
    override fun getContentPosition(): Long = 0L
    override fun getContentBufferedPosition(): Long = 0L
    override fun getAudioAttributes(): AudioAttributes = AudioAttributes.DEFAULT
    override fun setVolume(volume: Float) {}
    override fun getVolume(): Float = 1f
    override fun clearVideoSurface() {}
    override fun clearVideoSurface(surface: Surface?) {}
    override fun setVideoSurface(surface: Surface?) {}
    override fun setVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {}
    override fun clearVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {}
    override fun setVideoSurfaceView(surfaceView: SurfaceView?) {}
    override fun clearVideoSurfaceView(surfaceView: SurfaceView?) {}
    override fun setVideoTextureView(textureView: TextureView?) {}
    override fun clearVideoTextureView(textureView: TextureView?) {}
    override fun getVideoSize(): VideoSize = VideoSize.UNKNOWN
    override fun getSurfaceSize(): Size = Size.UNKNOWN
    override fun getCurrentCues(): CueGroup = CueGroup.EMPTY_TIME_ZERO
    override fun getDeviceInfo(): DeviceInfo = DeviceInfo.UNKNOWN
    override fun getDeviceVolume(): Int = 50
    override fun isDeviceMuted(): Boolean = false
    override fun setDeviceVolume(volume: Int) {}
    override fun setDeviceVolume(volume: Int, flags: Int) {}
    override fun increaseDeviceVolume() {}
    override fun increaseDeviceVolume(flags: Int) {}
    override fun decreaseDeviceVolume() {}
    override fun decreaseDeviceVolume(flags: Int) {}
    override fun setDeviceMuted(muted: Boolean) {}
    override fun setDeviceMuted(muted: Boolean, flags: Int) {}
    override fun setAudioAttributes(
        audioAttributes: AudioAttributes,
        handleAudioFocus: Boolean
    ) {
    }
}

@UnstableApi
class FakePlayerState : PlayerState {
    override val player: Player = FakePlayer()
    override val timeline: Timeline = Timeline.EMPTY
    override val mediaItemIndex: Int = 0
    override val tracks: Tracks = Tracks.EMPTY
    override val currentMediaItem: MediaItem = MockSongsDataSource().getAllSongs()[0].toMediaItem()
    override val mediaMetadata: MediaMetadata = MediaMetadata.EMPTY
    override val playlistMetadata: MediaMetadata = MediaMetadata.EMPTY
    override val isLoading: Boolean = false
    override val availableCommands: Player.Commands = Player.Commands.EMPTY
    override val trackSelectionParameters: TrackSelectionParameters =
        TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT
    override val playbackState: Int = Player.STATE_IDLE
    override val playWhenReady: Boolean = false
    override val playbackSuppressionReason: Int = Player.PLAYBACK_SUPPRESSION_REASON_NONE
    override val isPlaying: Boolean = false
    override val repeatMode: Int = Player.REPEAT_MODE_OFF
    override val shuffleModeEnabled: Boolean = false
    override val playerError: PlaybackException? = null
    override val playbackParameters: PlaybackParameters = PlaybackParameters.DEFAULT
    override val seekBackIncrement: Long = 5000
    override val seekForwardIncrement: Long = 5000
    override val maxSeekToPreviousPosition: Long = 0
    override val audioAttributes: AudioAttributes = AudioAttributes.DEFAULT
    override val volume: Float = 1f
    override val deviceInfo: DeviceInfo = DeviceInfo.UNKNOWN
    override val deviceVolume: Int = 0
    override val isDeviceMuted: Boolean = false
    override val videoSize: VideoSize = VideoSize.UNKNOWN
    override val cues: CueGroup = CueGroup.EMPTY_TIME_ZERO
    override fun dispose() {}
}