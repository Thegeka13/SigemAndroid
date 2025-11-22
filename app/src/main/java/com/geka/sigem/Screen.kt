package com.geka.sigem

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Market : Screen("market")
}
