package com.tariq.media3composemusicapp.presentation.screens

import android.icu.util.TimeUnit
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.tariq.media3composemusicapp.data.local.models.AudioItem
import com.tariq.media3composemusicapp.data.repository.MusicRepository
import com.tariq.media3composemusicapp.media_player.service.MusicServiceHandler
import com.tariq.media3composemusicapp.utils.HomeUIState
import com.tariq.media3composemusicapp.utils.HomeUiEvents
import com.tariq.media3composemusicapp.utils.MediaStateEvents
import com.tariq.media3composemusicapp.utils.MusicStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicServiceHandler: MusicServiceHandler,
    private val repository: MusicRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    var progress by savedStateHandle.saveable { mutableStateOf(0f) }
    var progressValue by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isMusicPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    var currentSelectedMusic by savedStateHandle.saveable { mutableStateOf(AudioItem(0L, "".toUri(), "", "", 0, "", "", null)) }
    var musicList by savedStateHandle.saveable { mutableStateOf(listOf<AudioItem>()) }

    private val _homeUiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState.InitialHome)
    val homeUIState: StateFlow<HomeUIState> = _homeUiState.asStateFlow()

    init {
        getMusicData()
    }

    init {
        viewModelScope.launch {
            musicServiceHandler.musicStates.collectLatest { musicStates: MusicStates ->
                when (musicStates) {
                    MusicStates.Initial -> _homeUiState.value = HomeUIState.InitialHome
                    is MusicStates.MediaBuffering -> progressCalculation(musicStates.progress)
                    is MusicStates.MediaPlaying -> isMusicPlaying = musicStates.isPlaying
                    is MusicStates.MediaProgress -> progressCalculation(musicStates.progress)
                    is MusicStates.CurrentMediaPlaying -> {
                        currentSelectedMusic = musicList[musicStates.mediaItemIndex]
                    }

                    is MusicStates.MediaReady -> {
                        duration = musicStates.duration
                        _homeUiState.value = HomeUIState.HomeReady

                    }

                }
            }
        }
    }

    fun onHomeUiEvents(homeUiEvents: HomeUiEvents) = viewModelScope.launch {
        when (homeUiEvents) {
            HomeUiEvents.Backward -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.Backward)
            HomeUiEvents.Forward -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.Forward)
            HomeUiEvents.SeekToNext -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.SeekToNext)
            HomeUiEvents.SeekToPrevious -> musicServiceHandler.onMediaStateEvents(MediaStateEvents.SeekToPrevious)
            is HomeUiEvents.PlayPause -> {
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.PlayPause
                )
            }

            is HomeUiEvents.SeekTo -> {
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.SeekTo,
                    seekPosition = ((duration * homeUiEvents.position) / 100f).toLong()
                )
            }

            is HomeUiEvents.CurrentAudioChanged -> {
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.SelectedMusicChange,
                    selectedMusicIndex = homeUiEvents.index
                )
            }

            is HomeUiEvents.UpdateProgress -> {
                musicServiceHandler.onMediaStateEvents(
                    MediaStateEvents.MediaProgress(
                        homeUiEvents.progress
                    )
                )
            }

        }
    }

    private fun getMusicData() {
        viewModelScope.launch {
            val musicData = repository.getAudioData()
            musicList = musicData
            setMusicItems()
        }
    }

    private fun setMusicItems() {
        musicList.map { audioItem ->
            MediaItem.Builder()
                .setUri(audioItem.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audioItem.artist)
                        .setDisplayTitle(audioItem.title)
                        .setSubtitle(audioItem.displayName)
                        .build()
                )
                .build()
        }.also {
            musicServiceHandler.setMediaItemList(it)
        }
    }

    private fun progressCalculation(currentProgress: Long) {
        progress = if (currentProgress > 0) ((currentProgress.toFloat() / duration.toFloat()) * 100f)
        else 0f

        progressValue = formatDurationValue(currentProgress)
    }

    private fun formatDurationValue(duration: Long): String {
        val minutes = MINUTES.convert(duration, MILLISECONDS)
        val seconds = (minutes) - minutes * SECONDS.convert(1, MINUTES)

        return String.format("%02d:%02d", minutes, seconds)
    }

}