package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.player.M3uParser
import com.example.player.PlayerManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("RAB 4K Dolby Media Player", appName)
    }

    @Test
    fun `test m3u playlist parser`() {
        val sampleM3u = """
            #EXTM3U
            #EXTINF:-1 group-title="Movies",Action Movie 4K
            https://example.com/movie.m3u8
            #EXTINF:-1 group-title="Sports",Live Stadium Stream
            https://example.com/sports.m3u8
        """.trimIndent()

        val channels = M3uParser.parse(sampleM3u)
        assertEquals(2, channels.size)
        assertEquals("Action Movie 4K", channels[0].name)
        assertEquals("https://example.com/movie.m3u8", channels[0].url)
        assertEquals("Movies", channels[0].group)
    }

    @Test
    fun `test duration formatting`() {
        assertEquals("00:00:00", PlayerManager.formatDuration(0L))
        assertEquals("00:11:43", PlayerManager.formatDuration(703000L))
        assertEquals("02:25:01", PlayerManager.formatDuration(8701000L))
    }
}
