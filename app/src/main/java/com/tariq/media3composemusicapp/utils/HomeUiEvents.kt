package com.tariq.media3composemusicapp.utils

sealed class HomeUiEvents {
    object PlayPause : HomeUiEvents()
    data class CurrentAudioChanged(val index: Int) : HomeUiEvents()
    data class SeekTo(val position: Float) : HomeUiEvents()
    data class UpdateProgress(val progress: Float) : HomeUiEvents()
    object SeekToNext : HomeUiEvents()
    object SeekToPrevious : HomeUiEvents()
    object Backward : HomeUiEvents()
    object Forward : HomeUiEvents()
}
