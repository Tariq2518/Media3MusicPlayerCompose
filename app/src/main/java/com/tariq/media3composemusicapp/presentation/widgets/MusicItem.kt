package com.tariq.media3composemusicapp.presentation.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tariq.media3composemusicapp.R
import com.tariq.media3composemusicapp.data.local.models.AudioItem
import kotlin.math.floor

@Composable
fun MusicItem(
    musicItem: AudioItem,
    onClickCallback: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.dp,
                vertical = 6.dp
            )
            .clickable {
                onClickCallback()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp
                )
        ) {
            Spacer(modifier = Modifier.size(4.dp))
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.music_icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.size(4.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    ),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = musicItem.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = musicItem.artist,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                )

            }
            Text(
                text = convertTimestampToDuration(musicItem.duration.toLong()),
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Clip,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.size(8.dp))
        }
    }

}

private fun convertTimestampToDuration(position: Long): String {
    val seconds = floor(position / 1E3).toInt()
    val minutes = seconds / 60
    val remainingTimeSeconds = seconds - (minutes * 60)
    return if (position < 0) "--:--" else "%d:%02d".format(minutes, remainingTimeSeconds)
}