package com.tariq.media3composemusicapp.presentation.screens.player

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tariq.media3composemusicapp.R
import com.tariq.media3composemusicapp.presentation.screens.home.HomeViewModel
import com.tariq.media3composemusicapp.presentation.widgets.MiniPlayer
import com.tariq.media3composemusicapp.utils.HomeUiEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text(text = "Music App") }
            )
        },

        ) {
        Column(
            Modifier.padding(it)
        ) {
            Spacer(modifier = Modifier.size(20.dp))
            AsyncImage(
                model = homeViewModel.currentSelectedMusic.artWork
                    ?: R.drawable.ic_launcher_background,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(16.dp)),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Slider(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                value = homeViewModel.progress,
                valueRange = 0f..100f,
                onValueChange = {
                    homeViewModel.onHomeUiEvents(HomeUiEvents.SeekTo(it))
                }
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .clickable {
                            homeViewModel.onHomeUiEvents(HomeUiEvents.SeekToPrevious)
                        }
                        .size(40.dp),
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "prev"
                )
                Spacer(modifier = Modifier.size(4.dp))
                MiniPlayerIcon(
                    modifier = Modifier
                        .size(60.dp),
                    icon = if (homeViewModel.isMusicPlaying) Icons.Default.Pause
                    else Icons.Default.PlayArrow
                ) {
                    homeViewModel.onHomeUiEvents(HomeUiEvents.PlayPause)
                }
                Spacer(modifier = Modifier.size(4.dp))
                Icon(
                    modifier = Modifier
                        .clickable {
                            homeViewModel.onHomeUiEvents(HomeUiEvents.SeekToNext)
                        }
                        .size(40.dp),
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "next"
                )
            }
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
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = icon,
                contentDescription = "player icon"
            )
        }
    }
}

@Preview
@Composable
private fun PlayerScreenPreview() {
    PlayerScreen()
}