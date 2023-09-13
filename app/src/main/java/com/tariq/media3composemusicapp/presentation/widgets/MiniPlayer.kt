package com.tariq.media3composemusicapp.presentation.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tariq.media3composemusicapp.data.local.models.AudioItem

@Composable
fun MiniPlayer(
    musicItem: AudioItem,
    progress: Float,
    onProgressCallback: (Float) -> Unit,
    isMusicPlaying: Boolean,
    onStartCallback: () -> Unit,
    onNextCallback: () -> Unit
) {
    BottomAppBar(
        content = {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArtistInfoTab(
                        audioItem = musicItem,
                        modifier = Modifier.weight(1f)
                    )
                    MiniPlayerControls(
                        isMusicPlaying = isMusicPlaying,
                        onStartCallback,
                        onNextCallback
                    )
                    Slider(
                        value = progress,
                        valueRange = 0f..100f,
                        onValueChange = {
                            onProgressCallback(it)
                        })
                }


            }
        }
    )

}

@Composable
fun MiniPlayerControls(
    isMusicPlaying: Boolean,
    onStartCallback: () -> Unit,
    onNextCallback: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MiniPlayerIcon(
            icon = if (isMusicPlaying) Icons.Default.Pause
            else Icons.Default.PlayArrow
        ) {
            onStartCallback()
        }
        Spacer(modifier = Modifier.size(4.dp))
        Icon(
            modifier = Modifier
                .clickable {
                    onNextCallback()
                },
            imageVector = Icons.Default.SkipNext,
            contentDescription = "next"
        )
    }

}

@Composable
fun ArtistInfoTab(
    audioItem: AudioItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MiniPlayerIcon(
            icon = Icons.Default.MusicNote,
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        ) {}
        Spacer(modifier = Modifier.size(4.dp))
        Column {
            Text(
                modifier = Modifier.weight(1f),
                text = audioItem.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = audioItem.artist,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
        }


    }

}

@Composable
fun MiniPlayerIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    borderStroke: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClickCallback: () -> Unit
) {

    Surface(
        shape = CircleShape,
        border = borderStroke,
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClickCallback()
            },
        contentColor = color,
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = "player icon")
        }

    }

}