package com.tariq.media3composemusicapp.utils

sealed class HomeUIState{
    object InitialHome: HomeUIState()
    object HomeReady: HomeUIState()
}
