package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import com.example.player.PlayerManager
import com.example.ui.PlayerScreen
import com.example.ui.theme.MyApplicationTheme

@UnstableApi
class MainActivity : ComponentActivity() {

    private lateinit var playerManager: PlayerManager

    // Storage permission launcher across Android 11, 12, 13, 14, 15, 16
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Permissions handled
    }

    // Document file picker for local videos and USB drives
    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            val title = it.lastPathSegment?.substringAfterLast('/')?.substringBeforeLast('.')
            playerManager.playUri(it.toString(), title)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen on during playback
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Android TV Fullscreen & Immersive Mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Request HDR mode if display supports it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.colorMode = ActivityInfo.COLOR_MODE_HDR
        }

        playerManager = PlayerManager(this)

        // Check & request appropriate storage permissions for Android 11-16
        requestStoragePermissions()

        // Handle incoming ACTION_VIEW intent if launched with a video file/stream
        handleIntent(intent)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    PlayerScreen(
                        playerManager = playerManager,
                        onOpenFilePicker = {
                            filePickerLauncher.launch(
                                arrayOf(
                                    "video/*",
                                    "audio/*",
                                    "application/octet-stream",
                                    "*/*"
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val uri = intent?.data
        if (uri != null) {
            val title = uri.lastPathSegment?.substringAfterLast('/')?.substringBeforeLast('.')
            playerManager.playUri(uri.toString(), title)
        } else {
            // Default demo media: 4K HDR stream matching reference UI
            val defaultUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            playerManager.playUri(defaultUrl, "SPIDER-MAN BRAND NEW DAY")
        }
    }

    private fun requestStoragePermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13-16 (API 33-36)
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            // Android 11-12 (API 30-32)
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val needed = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(this, it) != android.content.pm.PackageManager.PERMISSION_GRANTED
        }

        if (needed.isNotEmpty()) {
            permissionLauncher.launch(needed.toTypedArray())
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-hide system bars on resume
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
    }

    override fun onPause() {
        super.onPause()
        // Pause playback if exiting to background
        playerManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Hardware media keys mapping for Android TV remote
        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY -> {
                playerManager.play()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                playerManager.pause()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                playerManager.togglePlayPause()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_STOP -> {
                playerManager.stop()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {
                playerManager.forward10s()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_REWIND -> {
                playerManager.rewind10s()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_NEXT -> {
                playerManager.nextChapter()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                playerManager.previousChapter()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
