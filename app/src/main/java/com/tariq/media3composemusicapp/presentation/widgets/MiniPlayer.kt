package com.tariq.media3composemusicapp.presentation.widgets

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tariq.media3composemusicapp.R
import com.tariq.media3composemusicapp.data.local.models.AudioItem

@Composable
fun MiniPlayer(
    musicItem: AudioItem,
    progress: Float,
    onProgressCallback: (Float) -> Unit,
    isMusicPlaying: Boolean,
    onStartCallback: () -> Unit,
    onNextCallback: () -> Unit,
    onMiniPlayerClick: (musicItem: AudioItem) -> Unit

) {
    BottomAppBar(
        content = {
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .clickable {
                        onMiniPlayerClick.invoke(musicItem)
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArtistInfoTab(
                        audioItem = musicItem,
                        modifier = Modifier.weight(0.7f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    MiniPlayerControls(
                        modifier = Modifier.weight(0.15f),
                        isMusicPlaying = isMusicPlaying,
                        onStartCallback,
                        onNextCallback
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Slider(
                    value = progress,
                    valueRange = 0f..100f,
                    onValueChange = {
                        onProgressCallback(it)
                    }
                )
            }
        }
    )
}

@Composable
fun MiniPlayerControls(
    modifier: Modifier = Modifier,
    isMusicPlaying: Boolean,
    onStartCallback: () -> Unit,
    onNextCallback: () -> Unit,
) {
    Row(
        modifier = modifier
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
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (audioItem.artWork != null) {
            AsyncImage(
                model = audioItem.artWork,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        } else {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.music_icon),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
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
    onClickCallback: () -> Unit,
) {
    Surface(
        shape = CircleShape,
        border = borderStroke,
        modifier = modifier
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

@Preview
@Composable
fun PreviewMiniPlayer() {
    MaterialTheme {
        Surface(Modifier.background(MaterialTheme.colorScheme.background)) {
            MiniPlayer(
                musicItem = AudioItem(
                    0, Uri.parse(""), "Song Name", "Artist Name", 0, "title", "", null
                ),
                1.4f, { it }, false,
                onStartCallback = {},
                onNextCallback = {},
                onMiniPlayerClick = {}
            )
        }
    }
}
