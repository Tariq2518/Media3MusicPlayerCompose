package com.tariq.media3composemusicapp.presentation.screens

import androidx.compose.runtime.Composable
import com.tariq.media3composemusicapp.data.local.models.AudioItem

@Composable
fun HomeScreen(
    progress: Float,
    onProgressCallback: (Float) -> Unit,
    isMusicPlaying: Boolean,
    musicList: List<AudioItem>,
    onStartCallback: (Float) -> Unit,
    onMusicClick: (Int) -> Unit,
    onNextCallback: () -> Unit
) {

}