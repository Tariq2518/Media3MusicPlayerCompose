package com.tariq.media3composemusicapp.navigation

sealed class Screen(val route: String) {

    object HomeScreen : Screen("home_screen")
    object PlayerScreen : Screen("player_screen")

}