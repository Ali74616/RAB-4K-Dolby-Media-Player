package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChapterItem
import com.example.player.PlayerManager
import com.example.ui.theme.PlayerAccentBlue
import com.example.ui.theme.PlayerFocusRing
import com.example.ui.theme.PlayerWhite
import com.example.ui.theme.PlayerWhite40
import com.example.ui.theme.PlayerWhite70

/**
 * Bottom control overlay matching the reference image.
 * Progress bar with chapter ticks, seek handle, time code in center,
 * playback icons on left, and utility icons on right.
 */
@Composable
fun BottomControlsOverlay(
    isPlaying: Boolean,
    currentPositionMs: Long,
    durationMs: Long,
    isLive: Boolean,
    chapters: List<ChapterItem>,
    onPlayPause: () -> Unit,
    onStop: () -> Unit,
    onRewind: () -> Unit,
    onForward: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Long) -> Unit,
    onOpenInfo: () -> Unit,
    onOpenAudioTracks: () -> Unit,
    onOpenSubtitles: () -> Unit,
    onOpenAspectRatio: () -> Unit,
    onOpenSpeed: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenNetworkStream: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        // --- Full-width Progress Bar with Seek Handle and Chapter Ticks ---
        val progressFraction = if (durationMs > 0L) {
            (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
        } else 0f

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .pointerInput(durationMs) {
                    detectTapGestures { offset ->
                        if (durationMs > 0L) {
                            val newFraction = (offset.x / size.width).coerceIn(0f, 1f)
                            onSeek((newFraction * durationMs).toLong())
                        }
                    }
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // Background track + Progress + Chapter ticks Canvas
            Canvas(modifier = Modifier.fillMaxWidth().height(6.dp)) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Dark gray unplayed track
                drawRect(
                    color = Color(0x55FFFFFF),
                    topLeft = Offset(0f, 0f),
                    size = Size(canvasWidth, canvasHeight)
                )

                // Blue played track
                drawRect(
                    color = PlayerAccentBlue,
                    topLeft = Offset(0f, 0f),
                    size = Size(canvasWidth * progressFraction, canvasHeight)
                )

                // White chapter tick marks
                if (durationMs > 0L) {
                    for (ch in chapters) {
                        if (ch.startTimeMs > 0L && ch.startTimeMs < durationMs) {
                            val tickX = (ch.startTimeMs.toFloat() / durationMs.toFloat()) * canvasWidth
                            drawRect(
                                color = PlayerWhite,
                                topLeft = Offset(tickX - 1.5f, -2f),
                                size = Size(3f, canvasHeight + 4f)
                            )
                        }
                    }
                }
            }

            // Seek Handle "< >" matching reference image
            val handlePositionX = progressFraction
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // Seek Handle styled with < >
                Box(
                    modifier = Modifier
                        .padding(
                            start = (handlePositionX * 94).coerceIn(0f, 94f).dp // responsive handle
                        )
                        .background(PlayerWhite, RoundedCornerShape(2.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "< >",
                        color = Color.Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- Controls Row: Left controls | Time label in Center | Right controls ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Bottom-Left Controls: Prev, Rewind, Play/Pause, Stop, Forward, Next
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlayerIconButton(
                    icon = Icons.Filled.SkipPrevious,
                    contentDescription = "Previous Chapter",
                    onClick = onPrevious,
                    testTag = "btn_prev"
                )
                PlayerIconButton(
                    icon = Icons.Filled.FastRewind,
                    contentDescription = "Rewind 10 Seconds",
                    onClick = onRewind,
                    testTag = "btn_rewind"
                )
                PlayerIconButton(
                    icon = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    onClick = onPlayPause,
                    size = 38.dp,
                    testTag = "btn_play_pause"
                )
                PlayerIconButton(
                    icon = Icons.Filled.Stop,
                    contentDescription = "Stop",
                    onClick = onStop,
                    testTag = "btn_stop"
                )
                PlayerIconButton(
                    icon = Icons.Filled.FastForward,
                    contentDescription = "Fast Forward 10 Seconds",
                    onClick = onForward,
                    testTag = "btn_forward"
                )
                PlayerIconButton(
                    icon = Icons.Filled.SkipNext,
                    contentDescription = "Next Chapter",
                    onClick = onNext,
                    testTag = "btn_next"
                )
            }

            // Center Time Indicator: "00:11:43 / 02:25:01"
            val timeString = if (isLive) {
                "LIVE STREAM"
            } else {
                "${PlayerManager.formatDuration(currentPositionMs)} / ${PlayerManager.formatDuration(durationMs)}"
            }
            Text(
                text = timeString,
                color = PlayerWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )

            // Bottom-Right Controls: Stream, Info, Audio, Subtitle, Aspect, Speed, Settings
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlayerIconButton(
                    icon = Icons.Filled.Tv,
                    contentDescription = "Open Network Stream",
                    onClick = onOpenNetworkStream,
                    testTag = "btn_stream"
                )
                PlayerIconButton(
                    icon = Icons.Filled.Info,
                    contentDescription = "Stream Info",
                    onClick = onOpenInfo,
                    testTag = "btn_info"
                )
                PlayerIconButton(
                    icon = Icons.Filled.Headphones,
                    contentDescription = "Audio Tracks (Dual Audio)",
                    onClick = onOpenAudioTracks,
                    testTag = "btn_audio"
                )
                PlayerIconButton(
                    icon = Icons.Filled.Subtitles,
                    contentDescription = "Subtitles",
                    onClick = onOpenSubtitles,
                    testTag = "btn_subtitles"
                )
                PlayerIconButton(
                    icon = Icons.Filled.ZoomOutMap,
                    contentDescription = "Aspect Ratio / Size",
                    onClick = onOpenAspectRatio,
                    testTag = "btn_aspect"
                )
                PlayerIconButton(
                    icon = Icons.Filled.Speed,
                    contentDescription = "Playback Speed",
                    onClick = onOpenSpeed,
                    testTag = "btn_speed"
                )
                PlayerIconButton(
                    icon = Icons.Filled.Settings,
                    contentDescription = "Player Settings",
                    onClick = onOpenSettings,
                    testTag = "btn_settings"
                )
            }
        }
    }
}

/**
 * Flat white icon button with TV D-pad focus highlight.
 */
@Composable
fun PlayerIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 28.dp,
    testTag: String = ""
) {
    DpadFocusableBox(
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(24.dp),
        focusBorderColor = PlayerFocusRing,
        focusedBorderWidth = 2.dp
    ) { isFocused ->
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isFocused) PlayerFocusRing else PlayerWhite,
            modifier = Modifier.size(size)
        )
    }
}
