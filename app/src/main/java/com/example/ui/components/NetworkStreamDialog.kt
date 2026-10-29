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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ChannelItem
import com.example.player.M3uParser
import com.example.ui.theme.PlayerAccentBlue
import com.example.ui.theme.PlayerAccentLightBlue
import com.example.ui.theme.PlayerFocusRing
import com.example.ui.theme.PlayerSurfaceTranslucent
import com.example.ui.theme.PlayerWhite
import com.example.ui.theme.PlayerWhite40
import com.example.ui.theme.PlayerWhite70
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Screen / Dialog to open Network Streams (HTTP, HLS, DASH, RTSP, UDP),
 * M3U/M3U8 playlists, or local storage files.
 */
@Composable
fun NetworkStreamDialog(
    onPlayUri: (url: String, title: String?) -> Unit,
    onOpenFilePicker: () -> Unit,
    onDismiss: () -> Unit
) {
    var urlInput by remember { mutableStateOf("") }
    var parsedChannels by remember { mutableStateOf<List<ChannelItem>>(emptyList()) }
    var isLoadingPlaylist by remember { mutableStateOf(false) }
    var playlistError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val sampleStreams = remember {
        listOf(
            "4K HDR UHD Demo (H.265 / HEVC)" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            "Dolby Digital 5.1 Test (Elephants Dream)" to "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            "HLS Multi-Bitrate Stream (Tears of Steel)" to "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8",
            "DASH 4K Stream (Sintel)" to "https://dash.akamaized.net/akamai/test/caption_test/ElephantsDream/elephants_dream_480p_heaac5_1.mpd",
            "Live HLS Channel (Akamai Live)" to "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
        )
    }

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
                            imageVector = Icons.Filled.Tv,
                            contentDescription = null,
                            tint = PlayerAccentBlue,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Open Network Stream",
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
                    modifier = Modifier.padding(vertical = 14.dp),
                    color = Color(0x33FFFFFF)
                )

                // URL Input Field
                OutlinedTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    label = { Text("Stream URL (HTTP, HTTPS, HLS, DASH, RTSP, UDP)") },
                    placeholder = { Text("http://example.com/stream.m3u8", color = PlayerWhite40) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = PlayerWhite,
                        unfocusedTextColor = PlayerWhite,
                        focusedBorderColor = PlayerAccentBlue,
                        unfocusedBorderColor = Color(0x44FFFFFF),
                        focusedLabelColor = PlayerAccentBlue,
                        unfocusedLabelColor = PlayerWhite70
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            if (urlInput.isNotBlank()) {
                                onPlayUri(urlInput.trim(), null)
                                onDismiss()
                            }
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons: Play Stream & Open Local File & Parse M3U
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val trimmed = urlInput.trim()
                            if (trimmed.isNotBlank()) {
                                if (trimmed.endsWith(".m3u", ignoreCase = true) || trimmed.endsWith(".m3u8", ignoreCase = true)) {
                                    // Try loading playlist channels
                                    isLoadingPlaylist = true
                                    playlistError = null
                                    scope.launch {
                                        try {
                                            val client = OkHttpClient()
                                            val response = withContext(Dispatchers.IO) {
                                                client.newCall(Request.Builder().url(trimmed).build()).execute()
                                            }
                                            val body = response.body?.string() ?: ""
                                            val channels = M3uParser.parse(body)
                                            if (channels.isNotEmpty()) {
                                                parsedChannels = channels
                                            } else {
                                                // Single stream
                                                onPlayUri(trimmed, null)
                                                onDismiss()
                                            }
                                        } catch (e: Exception) {
                                            // Fallback to direct play
                                            onPlayUri(trimmed, null)
                                            onDismiss()
                                        } finally {
                                            isLoadingPlaylist = false
                                        }
                                    }
                                } else {
                                    onPlayUri(trimmed, null)
                                    onDismiss()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PlayerAccentBlue,
                            contentColor = PlayerWhite
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                        Text("Play Stream", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onOpenFilePicker,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x33FFFFFF),
                            contentColor = PlayerWhite
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Filled.FolderOpen, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                        Text("Local File / USB")
                    }
                }

                // If loading M3U
                if (isLoadingPlaylist) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            color = PlayerAccentBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.widthIn(8.dp))
                        Text("Parsing Playlist...", color = PlayerWhite70, fontSize = 14.sp)
                    }
                }

                // Parsed Playlist Channels
                if (parsedChannels.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "PLAYLIST CHANNELS (${parsedChannels.size})",
                        color = PlayerAccentLightBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        parsedChannels.forEach { channel ->
                            DpadFocusableBox(
                                onClick = {
                                    onPlayUri(channel.url, channel.name)
                                    onDismiss()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0x18FFFFFF), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(channel.name, color = PlayerWhite, fontSize = 14.sp)
                                    Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = PlayerAccentBlue)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Curated Sample 4K / Dolby Streams for Quick Testing
                Text(
                    text = "SAMPLE 4K & DOLBY DEMO STREAMS",
                    color = PlayerAccentLightBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                sampleStreams.forEach { (name, url) ->
                    DpadFocusableBox(
                        onClick = {
                            urlInput = url
                            onPlayUri(url, name)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0x14FFFFFF), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = name,
                                    color = PlayerWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = url,
                                    color = PlayerWhite40,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play",
                                tint = PlayerAccentBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
