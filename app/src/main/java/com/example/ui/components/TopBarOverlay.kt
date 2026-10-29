package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChapterItem
import com.example.ui.theme.PlayerAccentBlue
import com.example.ui.theme.PlayerWhite
import com.example.ui.theme.PlayerWhite70

/**
 * Top bar overlay matching the reference image:
 * Top-left: Title, Chapter indicator in blue & white.
 * Top-right: Current clock and Ends at time.
 */
@Composable
fun TopBarOverlay(
    title: String,
    currentChapter: ChapterItem?,
    currentTimeClock: String,
    endsAtClock: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // Top-Left: Title and Chapter
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = PlayerWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            if (currentChapter != null) {
                Spacer(modifier = Modifier.height(6.dp))
                val chapterText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = PlayerAccentBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    ) {
                        append("Chapter: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = PlayerWhite,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    ) {
                        append(currentChapter.title)
                    }
                }
                Text(text = chapterText)
            }
        }

        // Top-Right: Current Time & Ends At
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = currentTimeClock,
                color = PlayerWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = endsAtClock,
                color = PlayerWhite70,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
