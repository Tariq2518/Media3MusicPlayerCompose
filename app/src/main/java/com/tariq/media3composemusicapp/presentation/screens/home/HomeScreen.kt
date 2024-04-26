package com.tariq.media3composemusicapp.presentation.screens.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.tariq.media3composemusicapp.data.local.models.AudioItem
import com.tariq.media3composemusicapp.presentation.widgets.MiniPlayer
import com.tariq.media3composemusicapp.presentation.widgets.MusicItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    progress: Float,
    onProgressCallback: (Float) -> Unit,
    isMusicPlaying: Boolean,
    currentPlayingMusic: AudioItem,
    musicList: List<AudioItem>,
    onStartCallback: () -> Unit,
    onMusicClick: (Int) -> Unit,
    onNextCallback: () -> Unit,
    onMiniPlayerClickCallback: (musicItem: AudioItem) -> Unit
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
        bottomBar = {
            MiniPlayer(
                musicItem = currentPlayingMusic,
                progress = progress,
                isMusicPlaying = isMusicPlaying,
                onProgressCallback = onProgressCallback,
                onNextCallback = onNextCallback,
                onStartCallback = onStartCallback,
                onMiniPlayerClick = onMiniPlayerClickCallback
            )
        }
    ) {

        LazyColumn(
            contentPadding = it,
        ) {
            items(
                items = musicList,
                key = { item ->
                    item.id
                }
            ) { item ->
                MusicItem(item, onClickCallback = {
                    onMusicClick(musicList.indexOf(item))
                })
            }
        }
    }
}



