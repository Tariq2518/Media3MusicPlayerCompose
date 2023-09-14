package com.tariq.media3composemusicapp.utils

sealed class MusicStates {
    object Initial : MusicStates()
    data class MediaBuffering(val progress: Long) : MusicStates()
    data class MediaReady(val duration: Long) : MusicStates()
    data class MediaProgress(val progress: Long) : MusicStates()
    data class MediaPlaying(val isPlaying: Boolean) : MusicStates()
    data class CurrentMediaPlaying(val mediaItemIndex: Int) : MusicStates()
}
