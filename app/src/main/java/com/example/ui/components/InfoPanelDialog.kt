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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.MediaDetails
import com.example.ui.theme.PlayerAccentBlue
import com.example.ui.theme.PlayerAccentLightBlue
import com.example.ui.theme.PlayerFocusRing
import com.example.ui.theme.PlayerSurfaceTranslucent
import com.example.ui.theme.PlayerWhite
import com.example.ui.theme.PlayerWhite40
import com.example.ui.theme.PlayerWhite70

/**
 * Technical Info Panel displaying video, audio, HDR, and stream parameters.
 */
@Composable
fun InfoPanelDialog(
    details: MediaDetails,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .widthIn(max = 680.dp)
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
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = PlayerAccentBlue,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Stream & Decoder Details",
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
                    modifier = Modifier.padding(vertical = 14.dp),
                    color = Color(0x33FFFFFF)
                )

                // Video Section
                SectionHeader("VIDEO SPECIFICATIONS")
                InfoRow("Video Codec", details.videoCodec)
                InfoRow("Resolution", details.resolution)
                InfoRow("Frame Rate", details.frameRate)
                InfoRow("Video Bitrate", details.videoBitrate)
                InfoRow(
                    "HDR Format",
                    details.hdrType,
                    highlight = details.hdrType.contains("HDR") || details.hdrType.contains("Dolby")
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0x22FFFFFF)
                )

                // Audio Section
                SectionHeader("AUDIO & BITSTREAM OUTPUT")
                InfoRow("Audio Codec", details.audioCodec)
                InfoRow("Channels", details.channels)
                InfoRow("Sample Rate", details.sampleRate)
                InfoRow("Audio Bitrate", details.audioBitrate)
                InfoRow(
                    "Audio Output Mode",
                    details.audioOutputMode,
                    highlight = details.audioOutputMode.contains("Passthrough")
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0x22FFFFFF)
                )

                // Container & Stream Section
                SectionHeader("CONTAINER & NETWORK")
                InfoRow("Container", details.containerFormat)
                InfoRow("Buffer Status", details.fileSizeOrBuffer)
                InfoRow("Playback Type", if (details.isLive) "Live Transmission" else "VOD / Media File")

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PlayerAccentBlue,
                        contentColor = PlayerWhite
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = PlayerAccentLightBlue,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun InfoRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = PlayerWhite70,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = if (highlight) PlayerAccentBlue else PlayerWhite,
            fontSize = 14.sp,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}
