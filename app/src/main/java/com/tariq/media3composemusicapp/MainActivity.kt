package com.tariq.media3composemusicapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.tariq.media3composemusicapp.media_player.service.MediaService
import com.tariq.media3composemusicapp.presentation.screens.home.HomeScreen
import com.tariq.media3composemusicapp.presentation.screens.home.HomeViewModel
import com.tariq.media3composemusicapp.ui.theme.Media3ComposeMusicAppTheme
import com.tariq.media3composemusicapp.utils.HomeUiEvents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var isServiceRunning = false

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Media3ComposeMusicAppTheme {
                val isPermissionGranted = rememberPermissionState(
                    permission = if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE
                )
                val lifeCycleOwner = LocalLifecycleOwner.current

                DisposableEffect(key1 = lifeCycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            isPermissionGranted.launchPermissionRequest()
                        }
                    }
                    lifeCycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifeCycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        progress = homeViewModel.progress,
                        onProgressCallback = {
                            homeViewModel.onHomeUiEvents(HomeUiEvents.SeekTo(it))
                        },
                        isMusicPlaying = homeViewModel.isMusicPlaying,
                        currentPlayingMusic = homeViewModel.currentSelectedMusic,
                        musicList = homeViewModel.musicList,
                        onStartCallback = {
                            homeViewModel.onHomeUiEvents(HomeUiEvents.PlayPause)
                        },
                        onMusicClick = {
                            homeViewModel.onHomeUiEvents(HomeUiEvents.CurrentAudioChanged(it))
                            startMusicService()
                        },
                        onNextCallback = {
                            homeViewModel.onHomeUiEvents(HomeUiEvents.SeekToNext)
                        }

                    )

                }

            }
        }
    }

    private fun startMusicService() {
        if (!isServiceRunning) {
            val intent = Intent(this, MediaService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            isServiceRunning = true
        }
    }
}

