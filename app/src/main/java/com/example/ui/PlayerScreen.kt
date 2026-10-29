package com.example.ui

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.model.AspectRatioMode
import com.example.player.PlayerManager
import com.example.ui.components.AspectRatioDialog
import com.example.ui.components.AudioTrackDialog
import com.example.ui.components.BottomControlsOverlay
import com.example.ui.components.InfoPanelDialog
import com.example.ui.components.NetworkStreamDialog
import com.example.ui.components.PlaybackSpeedDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.components.SubtitleDialog
import com.example.ui.components.TopBarOverlay
import com.example.ui.theme.PlayerAccentBlue
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    playerManager: PlayerManager,
    onOpenFilePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlaying by playerManager.isPlaying.collectAsState()
    val playbackState by playerManager.playbackState.collectAsState()
    val currentPositionMs by playerManager.currentPosition.collectAsState()
    val durationMs by playerManager.duration.collectAsState()
    val isLive by playerManager.isLive.collectAsState()
    val title by playerManager.mediaTitle.collectAsState()
    val currentChapter by playerManager.currentChapter.collectAsState()
    val chapters by playerManager.chapters.collectAsState()
    val currentTimeClock by playerManager.currentTimeClock.collectAsState()
    val endsAtClock by playerManager.endsAtClock.collectAsState()
    val mediaDetails by playerManager.mediaDetails.collectAsState()
    val audioTracks by playerManager.audioTracks.collectAsState()
    val subtitleTracks by playerManager.subtitleTracks.collectAsState()
    val aspectRatioMode by playerManager.aspectRatioMode.collectAsState()
    val playbackSpeed by playerManager.playbackSpeed.collectAsState()
    val audioOutputPref by playerManager.audioOutputPref.collectAsState()
    val audioDelayMs by playerManager.audioDelayMs.collectAsState()
    val subtitleSizeSp by playerManager.subtitleSizeSp.collectAsState()
    val decoderPref by playerManager.decoderPreference.collectAsState()

    // OSD Visibility & Auto-hide
    var isControlsVisible by remember { mutableStateOf(true) }
    var lastInteractionTime by remember { mutableStateOf(System.currentTimeMillis()) }

    // Dialogs state
    var showInfoDialog by remember { mutableStateOf(false) }
    var showAudioDialog by remember { mutableStateOf(false) }
    var showSubtitleDialog by remember { mutableStateOf(false) }
    var showAspectDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showNetworkDialog by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    // Function to wake/show OSD and reset timer
    fun pingControls() {
        isControlsVisible = true
        lastInteractionTime = System.currentTimeMillis()
    }

    // Auto-hide OSD after 5 seconds of inactivity when video is actively playing
    LaunchedEffect(isControlsVisible, lastInteractionTime, isPlaying) {
        if (isControlsVisible && isPlaying && !showInfoDialog && !showAudioDialog &&
            !showSubtitleDialog && !showAspectDialog && !showSpeedDialog &&
            !showSettingsDialog && !showNetworkDialog
        ) {
            delay(5000)
            isControlsVisible = false
        }
    }

    // BackHandler for TV remote BACK key
    BackHandler(enabled = true) {
        when {
            showInfoDialog -> showInfoDialog = false
            showAudioDialog -> showAudioDialog = false
            showSubtitleDialog -> showSubtitleDialog = false
            showAspectDialog -> showAspectDialog = false
            showSpeedDialog -> showSpeedDialog = false
            showSettingsDialog -> showSettingsDialog = false
            showNetworkDialog -> showNetworkDialog = false
            isControlsVisible -> isControlsVisible = false
            else -> {
                // If controls hidden and user presses back, reveal controls
                pingControls()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (isControlsVisible) {
                    isControlsVisible = false
                } else {
                    pingControls()
                }
            }
            .onKeyEvent { keyEvent ->
                if (keyEvent.type != KeyEventType.KeyDown) return@onKeyEvent false
                pingControls()

                when (keyEvent.key) {
                    Key.DirectionLeft -> {
                        if (!isControlsVisible) {
                            playerManager.rewind10s()
                            true
                        } else false
                    }
                    Key.DirectionRight -> {
                        if (!isControlsVisible) {
                            playerManager.forward10s()
                            true
                        } else false
                    }
                    Key.MediaPlayPause -> {
                        playerManager.togglePlayPause()
                        true
                    }
                    Key.MediaPlay -> {
                        playerManager.play()
                        true
                    }
                    Key.MediaPause -> {
                        playerManager.pause()
                        true
                    }
                    Key.MediaStop -> {
                        playerManager.stop()
                        true
                    }
                    Key.MediaFastForward -> {
                        playerManager.forward10s()
                        true
                    }
                    Key.MediaRewind -> {
                        playerManager.rewind10s()
                        true
                    }
                    Key.MediaNext -> {
                        playerManager.nextChapter()
                        true
                    }
                    Key.MediaPrevious -> {
                        playerManager.previousChapter()
                        true
                    }
                    else -> false
                }
            }
    ) {
        // Video Surface via Media3 PlayerView
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false
                    setShutterBackgroundColor(android.graphics.Color.BLACK)
                    player = playerManager.getPlayer()
                }
            },
            update = { playerView ->
                playerView.player = playerManager.getPlayer()
                playerView.resizeMode = when (aspectRatioMode) {
                    AspectRatioMode.FIT -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                    AspectRatioMode.FILL -> AspectRatioFrameLayout.RESIZE_MODE_FILL
                    AspectRatioMode.ZOOM -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    AspectRatioMode.RATIO_16_9 -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    AspectRatioMode.RATIO_4_3 -> AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Buffering Indicator
        if (playbackState == Player.STATE_BUFFERING) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = PlayerAccentBlue,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(54.dp)
                )
            }
        }

        // OSD Controls Overlay (auto-hides and reappears)
        AnimatedVisibility(
            visible = isControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x77000000))
            ) {
                // Top Bar: Video Title, Chapter info, Clock, Ends At
                TopBarOverlay(
                    title = title,
                    currentChapter = currentChapter,
                    currentTimeClock = currentTimeClock,
                    endsAtClock = endsAtClock,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                // Bottom Controls: Progress bar, Seek handle, Left controls, Center time, Right controls
                BottomControlsOverlay(
                    isPlaying = isPlaying,
                    currentPositionMs = currentPositionMs,
                    durationMs = durationMs,
                    isLive = isLive,
                    chapters = chapters,
                    onPlayPause = { playerManager.togglePlayPause(); pingControls() },
                    onStop = { playerManager.stop(); pingControls() },
                    onRewind = { playerManager.rewind10s(); pingControls() },
                    onForward = { playerManager.forward10s(); pingControls() },
                    onPrevious = { playerManager.previousChapter(); pingControls() },
                    onNext = { playerManager.nextChapter(); pingControls() },
                    onSeek = { pos -> playerManager.seekToPosition(pos); pingControls() },
                    onOpenInfo = { showInfoDialog = true; pingControls() },
                    onOpenAudioTracks = { showAudioDialog = true; pingControls() },
                    onOpenSubtitles = { showSubtitleDialog = true; pingControls() },
                    onOpenAspectRatio = { showAspectDialog = true; pingControls() },
                    onOpenSpeed = { showSpeedDialog = true; pingControls() },
                    onOpenSettings = { showSettingsDialog = true; pingControls() },
                    onOpenNetworkStream = { showNetworkDialog = true; pingControls() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        // Modal Dialogs
        if (showInfoDialog) {
            InfoPanelDialog(
                details = mediaDetails,
                onDismiss = { showInfoDialog = false; pingControls() }
            )
        }

        if (showAudioDialog) {
            AudioTrackDialog(
                tracks = audioTracks,
                onSelectTrack = { gIdx, tIdx -> playerManager.selectAudioTrack(gIdx, tIdx) },
                onDismiss = { showAudioDialog = false; pingControls() }
            )
        }

        if (showSubtitleDialog) {
            SubtitleDialog(
                tracks = subtitleTracks,
                onSelectTrack = { gIdx, tIdx -> playerManager.selectSubtitleTrack(gIdx, tIdx) },
                onDismiss = { showSubtitleDialog = false; pingControls() }
            )
        }

        if (showAspectDialog) {
            AspectRatioDialog(
                currentMode = aspectRatioMode,
                onSelectMode = { playerManager.setAspectRatio(it) },
                onDismiss = { showAspectDialog = false; pingControls() }
            )
        }

        if (showSpeedDialog) {
            PlaybackSpeedDialog(
                currentSpeed = playbackSpeed,
                onSelectSpeed = { playerManager.setPlaybackSpeed(it) },
                onDismiss = { showSpeedDialog = false; pingControls() }
            )
        }

        if (showSettingsDialog) {
            SettingsDialog(
                currentAudioOutput = audioOutputPref,
                onSelectAudioOutput = { playerManager.setAudioOutputPreference(it) },
                currentAudioDelayMs = audioDelayMs,
                onChangeAudioDelay = { playerManager.setAudioDelayMs(it) },
                currentSubtitleSizeSp = subtitleSizeSp,
                onSelectSubtitleSize = { playerManager.setSubtitleSizeSp(it) },
                currentDecoderPref = decoderPref,
                onSelectDecoderPref = { playerManager.setDecoderPreference(it) },
                onDismiss = { showSettingsDialog = false; pingControls() }
            )
        }

        if (showNetworkDialog) {
            NetworkStreamDialog(
                onPlayUri = { url, streamTitle ->
                    playerManager.playUri(url, streamTitle)
                },
                onOpenFilePicker = onOpenFilePicker,
                onDismiss = { showNetworkDialog = false; pingControls() }
            )
        }
    }
}
