package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AspectRatioMode
import com.example.model.AudioOutputPreference
import com.example.model.DecoderPreference
import com.example.ui.theme.PlayerAccentBlue
import com.example.ui.theme.PlayerAccentLightBlue
import com.example.ui.theme.PlayerFocusRing
import com.example.ui.theme.PlayerSurfaceTranslucent
import com.example.ui.theme.PlayerWhite
import com.example.ui.theme.PlayerWhite70

/**
 * Aspect Ratio / Size Dialog (Fit, Fill, Zoom, 16:9, 4:3).
 */
@Composable
fun AspectRatioDialog(
    currentMode: AspectRatioMode,
    onSelectMode: (AspectRatioMode) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .background(PlayerSurfaceTranslucent, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0x44FFFFFF), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ZoomOutMap,
                            contentDescription = null,
                            tint = PlayerAccentBlue,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Aspect Ratio / Size",
                            color = PlayerWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DpadFocusableBox(onClick = onDismiss, shape = RoundedCornerShape(20.dp)) { isFocused ->
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = if (isFocused) PlayerFocusRing else PlayerWhite,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0x33FFFFFF)
                )

                AspectRatioMode.values().forEach { mode ->
                    val isSelected = (mode == currentMode)
                    DpadFocusableBox(
                        onClick = {
                            onSelectMode(mode)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) Color(0x331E9BD7) else Color(0x11FFFFFF),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = mode.label,
                                color = if (isSelected) PlayerAccentBlue else PlayerWhite,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Selected",
                                    tint = PlayerAccentBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Playback Speed Dialog (0.5x, 0.75x, 1x, 1.25x, 1.5x, 2x).
 */
@Composable
fun PlaybackSpeedDialog(
    currentSpeed: Float,
    onSelectSpeed: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val speedOptions = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f)

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .background(PlayerSurfaceTranslucent, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0x44FFFFFF), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Speed,
                            contentDescription = null,
                            tint = PlayerAccentBlue,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Playback Speed",
                            color = PlayerWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DpadFocusableBox(onClick = onDismiss, shape = RoundedCornerShape(20.dp)) { isFocused ->
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = if (isFocused) PlayerFocusRing else PlayerWhite,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0x33FFFFFF)
                )

                speedOptions.forEach { speed ->
                    val isSelected = (speed == currentSpeed)
                    val label = if (speed == 1.0f) "1.0x (Normal)" else "${speed}x"

                    DpadFocusableBox(
                        onClick = {
                            onSelectSpeed(speed)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) Color(0x331E9BD7) else Color(0x11FFFFFF),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) PlayerAccentBlue else PlayerWhite,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Selected",
                                    tint = PlayerAccentBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Settings Dialog:
 * Audio output mode (Auto / Passthrough / Decode),
 * Audio delay (-500ms to +500ms),
 * Subtitle size (Small / Medium / Large / Extra Large),
 * Hardware / Software decoding preference.
 */
@Composable
fun SettingsDialog(
    currentAudioOutput: AudioOutputPreference,
    onSelectAudioOutput: (AudioOutputPreference) -> Unit,
    currentAudioDelayMs: Long,
    onChangeAudioDelay: (Long) -> Unit,
    currentSubtitleSizeSp: Float,
    onSelectSubtitleSize: (Float) -> Unit,
    currentDecoderPref: DecoderPreference,
    onSelectDecoderPref: (DecoderPreference) -> Unit,
    onDismiss: () -> Unit
) {
    var delayState by remember { mutableStateOf(currentAudioDelayMs) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .background(PlayerSurfaceTranslucent, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0x44FFFFFF), RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                            tint = PlayerAccentBlue,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Player & Audio Settings",
                            color = PlayerWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DpadFocusableBox(onClick = onDismiss, shape = RoundedCornerShape(20.dp)) { isFocused ->
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = if (isFocused) PlayerFocusRing else PlayerWhite,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0x33FFFFFF)
                )

                // 1. Audio Output Mode
                Text(
                    text = "AUDIO OUTPUT ROUTING",
                    color = PlayerAccentLightBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                AudioOutputPreference.values().forEach { pref ->
                    val isSelected = (pref == currentAudioOutput)
                    DpadFocusableBox(
                        onClick = { onSelectAudioOutput(pref) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) Color(0x331E9BD7) else Color(0x11FFFFFF),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pref.label,
                                color = if (isSelected) PlayerAccentBlue else PlayerWhite,
                                fontSize = 14.sp
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = PlayerAccentBlue
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Audio Delay
                Text(
                    text = "AUDIO DELAY / SYNC: ${delayState} ms",
                    color = PlayerAccentLightBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DpadFocusableBox(
                        onClick = {
                            delayState = (delayState - 50L).coerceAtLeast(-500L)
                            onChangeAudioDelay(delayState)
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("-50ms", color = PlayerWhite, modifier = Modifier.padding(8.dp))
                    }

                    DpadFocusableBox(
                        onClick = {
                            delayState = 0L
                            onChangeAudioDelay(0L)
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Reset (0ms)", color = PlayerAccentBlue, modifier = Modifier.padding(8.dp))
                    }

                    DpadFocusableBox(
                        onClick = {
                            delayState = (delayState + 50L).coerceAtMost(500L)
                            onChangeAudioDelay(delayState)
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("+50ms", color = PlayerWhite, modifier = Modifier.padding(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Subtitle Size
                Text(
                    text = "SUBTITLE SIZE",
                    color = PlayerAccentLightBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(14f to "Small", 18f to "Medium", 24f to "Large", 30f to "Extra Large").forEach { (size, label) ->
                        val isSelected = (currentSubtitleSizeSp == size)
                        DpadFocusableBox(
                            onClick = { onSelectSubtitleSize(size) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) Color(0x331E9BD7) else Color(0x11FFFFFF),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) PlayerAccentBlue else PlayerWhite,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. Hardware / Software Decoding
                Text(
                    text = "VIDEO DECODER PREFERENCE",
                    color = PlayerAccentLightBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                DecoderPreference.values().forEach { pref ->
                    val isSelected = (pref == currentDecoderPref)
                    DpadFocusableBox(
                        onClick = { onSelectDecoderPref(pref) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) Color(0x331E9BD7) else Color(0x11FFFFFF),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pref.label,
                                color = if (isSelected) PlayerAccentBlue else PlayerWhite,
                                fontSize = 14.sp
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = PlayerAccentBlue
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PlayerAccentBlue,
                        contentColor = PlayerWhite
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("DONE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
