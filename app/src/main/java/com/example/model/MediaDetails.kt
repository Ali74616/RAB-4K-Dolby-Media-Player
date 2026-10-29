package com.example.model

/**
 * Technical stream and playback parameters for display in the Info Panel.
 */
data class MediaDetails(
    val title: String = "",
    val videoCodec: String = "Detecting...",
    val resolution: String = "--",
    val frameRate: String = "--",
    val videoBitrate: String = "--",
    val hdrType: String = "SDR", // SDR, HDR10, HDR10+, HLG, Dolby Vision
    val audioCodec: String = "Detecting...",
    val channels: String = "--",
    val sampleRate: String = "--",
    val audioBitrate: String = "--",
    val audioOutputMode: String = "PCM Decoded", // Bitstream Passthrough (Direct HDMI) or PCM Decoded
    val containerFormat: String = "Auto / Stream",
    val fileSizeOrBuffer: String = "--",
    val isLive: Boolean = false
)

/**
 * Chapter marker inside media (e.g. MKV/MP4 chapters).
 */
data class ChapterItem(
    val index: Int,
    val title: String,
    val startTimeMs: Long,
    val endTimeMs: Long
)

/**
 * Track choice for audio or subtitles.
 */
data class MediaTrackChoice(
    val groupIndex: Int,
    val trackIndex: Int,
    val id: String,
    val label: String,
    val language: String,
    val mimeType: String,
    val isSelected: Boolean,
    val channelCount: Int = 0,
    val sampleRate: Int = 0
)

/**
 * Aspect Ratio / Resize mode.
 */
enum class AspectRatioMode(val label: String) {
    FIT("Fit (Letterbox)"),
    FILL("Fill (Stretch)"),
    ZOOM("Zoom (Crop)"),
    RATIO_16_9("16:9 Standard"),
    RATIO_4_3("4:3 Classic")
}

/**
 * Audio output routing mode.
 */
enum class AudioOutputPreference(val label: String) {
    AUTO("Auto (Receiver Detect)"),
    PASSTHROUGH("HDMI Bitstream Passthrough"),
    DECODE("PCM Decode & Downmix")
}

/**
 * Hardware vs software decoding preference.
 */
enum class DecoderPreference(val label: String) {
    HARDWARE("Hardware Accelerated (Recommended)"),
    SOFTWARE_FALLBACK("Software Fallback")
}

/**
 * M3U Playlist channel.
 */
data class ChannelItem(
    val name: String,
    val url: String,
    val group: String = "General"
)
