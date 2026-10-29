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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.MediaTrackChoice
import com.example.ui.theme.PlayerAccentBlue
import com.example.ui.theme.PlayerFocusRing
import com.example.ui.theme.PlayerSurfaceTranslucent
import com.example.ui.theme.PlayerWhite
import com.example.ui.theme.PlayerWhite40
import com.example.ui.theme.PlayerWhite70

/**
 * Audio Track Selection Dialog for multi/dual audio switching.
 */
@Composable
fun AudioTrackDialog(
    tracks: List<MediaTrackChoice>,
    onSelectTrack: (groupIndex: Int, trackIndex: Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .widthIn(max = 520.dp)
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
                            imageVector = Icons.Filled.Headphones,
                            contentDescription = null,
                            tint = PlayerAccentBlue,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Audio Tracks (Dual Audio)",
                            color = PlayerWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DpadFocusableBox(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(20.dp)
                    ) { isFocused ->
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

                if (tracks.isEmpty()) {
                    Text(
                        text = "No additional audio tracks found.",
                        color = PlayerWhite70,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tracks) { track ->
                            DpadFocusableBox(
                                onClick = {
                                    onSelectTrack(track.groupIndex, track.trackIndex)
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) { isFocused ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (track.isSelected) Color(0x331E9BD7) else Color(0x11FFFFFF),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = track.label,
                                            color = if (track.isSelected) PlayerAccentBlue else PlayerWhite,
                                            fontSize = 15.sp,
                                            fontWeight = if (track.isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                        Text(
                                            text = "Lang: ${track.language} • ${track.mimeType.substringAfter('/')}",
                                            color = PlayerWhite70,
                                            fontSize = 12.sp
                                        )
                                    }

                                    if (track.isSelected) {
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
    }
}

/**
 * Subtitles Selection Dialog.
 */
@Composable
fun SubtitleDialog(
    tracks: List<MediaTrackChoice>,
    onSelectTrack: (groupIndex: Int, trackIndex: Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .widthIn(max = 520.dp)
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
                            imageVector = Icons.Filled.Subtitles,
                            contentDescription = null,
                            tint = PlayerAccentBlue,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Subtitles",
                            color = PlayerWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DpadFocusableBox(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(20.dp)
                    ) { isFocused ->
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

                val isOffSelected = tracks.none { it.isSelected }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Option: Off
                    item {
                        DpadFocusableBox(
                            onClick = {
                                onSelectTrack(-1, -1)
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isOffSelected) Color(0x331E9BD7) else Color(0x11FFFFFF),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Off (Subtitles Disabled)",
                                    color = if (isOffSelected) PlayerAccentBlue else PlayerWhite,
                                    fontSize = 15.sp,
                                    fontWeight = if (isOffSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isOffSelected) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Selected",
                                        tint = PlayerAccentBlue
                                    )
                                }
                            }
                        }
                    }

                    items(tracks) { track ->
                        DpadFocusableBox(
                            onClick = {
                                onSelectTrack(track.groupIndex, track.trackIndex)
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (track.isSelected) Color(0x331E9BD7) else Color(0x11FFFFFF),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = track.label,
                                        color = if (track.isSelected) PlayerAccentBlue else PlayerWhite,
                                        fontSize = 15.sp,
                                        fontWeight = if (track.isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(
                                        text = "Lang: ${track.language} • ${track.mimeType.substringAfter('/')}",
                                        color = PlayerWhite70,
                                        fontSize = 12.sp
                                    )
                                }

                                if (track.isSelected) {
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
}
