package com.tariq.media3composemusicapp.media_player.service

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.tariq.media3composemusicapp.utils.MediaStateEvents
import com.tariq.media3composemusicapp.utils.MusicStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer
) : Player.Listener {

    private val _musicStates: MutableStateFlow<MusicStates> = MutableStateFlow(MusicStates.Initial)
    val musicStates: StateFlow<MusicStates> = _musicStates.asStateFlow()

    private var job: Job? = null

    fun setMediaItem(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    suspend fun onMediaStateEvents(
        mediaStateEvents: MediaStateEvents,
        selectedMusicIndex: Int = -1,
        seekPosition: Long = 0
    ) {
        when (mediaStateEvents) {
            MediaStateEvents.Backward -> exoPlayer.seekBack()
            MediaStateEvents.Forward -> exoPlayer.seekForward()
            MediaStateEvents.PlayPause -> playPauseMusic()
            MediaStateEvents.SeekTo -> exoPlayer.seekTo(seekPosition)
            MediaStateEvents.SeekToNext -> exoPlayer.seekToNext()
            MediaStateEvents.SeekToPrevious -> exoPlayer.seekToPrevious()
            MediaStateEvents.Stop -> stopProgressUpdate()
            MediaStateEvents.SelectedMusicChange -> {
                when (selectedMusicIndex) {
                    exoPlayer.currentMediaItemIndex -> {
                        playPauseMusic()
                    }

                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedMusicIndex)
                        _musicStates.value = MusicStates.MediaPlaying(
                            isPlaying = true
                        )
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            is MediaStateEvents.MediaProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * mediaStateEvents.progress).toLong()
                )
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _musicStates.value = MusicStates.MediaBuffering(exoPlayer.currentPosition)
            ExoPlayer.STATE_READY -> _musicStates.value = MusicStates.MediaReady(exoPlayer.duration)

        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _musicStates.value = MusicStates.MediaPlaying(isPlaying = isPlaying)
        _musicStates.value = MusicStates.CurrentMediaPlaying(exoPlayer.currentMediaItemIndex)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.IO) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun playPauseMusic() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            exoPlayer.play()
            _musicStates.value = MusicStates.MediaPlaying(
                isPlaying = true
            )
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            _musicStates.value = MusicStates.MediaProgress(exoPlayer.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _musicStates.value = MusicStates.MediaPlaying(isPlaying = false)
    }
}