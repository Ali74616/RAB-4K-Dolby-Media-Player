# ProGuard rules for RAB 4K Dolby Media Player

# Keep Media3 ExoPlayer classes and internal reflections
-keep class androidx.media3.exoplayer.** { *; }
-keep class androidx.media3.common.** { *; }
-keep class androidx.media3.ui.** { *; }
-keep class androidx.media3.datasource.** { *; }

# Keep AudioCapabilities, AudioSink and bitstream passthrough
-keep class android.media.AudioTrack { *; }
-keep class android.media.AudioFormat { *; }
-keep class android.media.AudioCapabilities { *; }
-keep class androidx.media3.exoplayer.audio.DefaultAudioSink { *; }
-keep class androidx.media3.exoplayer.audio.AudioCapabilities { *; }
-keep class androidx.media3.exoplayer.audio.AudioSink { *; }

# Keep MediaCodec decoders (Dolby Vision, HEVC, VP9, AV1, AC-3, E-AC-3, DTS)
-keep class android.media.MediaCodec** { *; }
-keep class android.media.MediaFormat { *; }

# Kotlin coroutines and Compose
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
