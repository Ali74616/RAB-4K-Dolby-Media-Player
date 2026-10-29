package com.example.player

import com.example.model.ChannelItem
import java.io.BufferedReader
import java.io.StringReader

/**
 * Robust parser for M3U and M3U8 IPTV/VOD playlists.
 * Extracts channel name, optional group, and stream URL.
 */
object M3uParser {

    fun parse(content: String): List<ChannelItem> {
        val channels = mutableListOf<ChannelItem>()
        val reader = BufferedReader(StringReader(content))
        var currentName: String? = null
        var currentGroup = "General"

        reader.useLines { lines ->
            for (rawLine in lines) {
                val line = rawLine.trim()
                if (line.isEmpty()) continue

                if (line.startsWith("#EXTINF:", ignoreCase = true)) {
                    // Example: #EXTINF:-1 group-title="Sports",ESPN HD
                    val commaIdx = line.indexOf(',')
                    currentName = if (commaIdx != -1 && commaIdx < line.length - 1) {
                        line.substring(commaIdx + 1).trim()
                    } else {
                        "Channel ${channels.size + 1}"
                    }

                    // Extract group-title attribute if present
                    val groupMatch = Regex("""group-title="([^"]+)"""").find(line)
                    currentGroup = groupMatch?.groupValues?.getOrNull(1) ?: "General"
                } else if (!line.startsWith("#")) {
                    // Stream URL
                    val name = currentName ?: "Channel ${channels.size + 1}"
                    channels.add(ChannelItem(name = name, url = line, group = currentGroup))
                    currentName = null
                    currentGroup = "General"
                }
            }
        }

        return channels
    }
}
