package com.tariq.media3composemusicapp.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tariq.media3composemusicapp.presentation.screens.player.PlayerScreen
import com.tariq.media3composemusicapp.presentation.screens.home.HomeScreen
import com.tariq.media3composemusicapp.presentation.screens.home.HomeViewModel
import com.tariq.media3composemusicapp.utils.HomeUiEvents

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    startMusicServiceCallback: () -> Unit,

    ) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.HomeScreen.route) {
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
                    startMusicServiceCallback.invoke()
                },
                onNextCallback = {
                    homeViewModel.onHomeUiEvents(HomeUiEvents.SeekToNext)
                },
                {
                    navController.navigate(Screen.PlayerScreen.route)
                }
            )
        }



        composable(
            route = Screen.PlayerScreen.route
        ) {

        PlayerScreen()
    }

    }
}