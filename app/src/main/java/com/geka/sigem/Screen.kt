package com.geka.sigem

sealed class Screen(val route: String) {

    // ----------------------
    // AUTENTICACIÓN
    // ----------------------
    object Login : Screen("login")

    // ----------------------
    // PRINCIPAL
    // ----------------------
    object Home : Screen("home")

    // ----------------------
    // MARKETPLACE
    // ----------------------
    object Market : Screen("market")
    object MarketNewPost : Screen("market/new")
    object MarketMyPosts : Screen("market/myposts")
    object MarketDetail : Screen("market/detail/{id}")

    // ----------------------
    // CURSOS
    // ----------------------
    object Cursos : Screen("cursos")
    object CursoDetalle : Screen("curso/{id}")


    // ----------------------
    // APOYOS
    // ----------------------
    object Apoyos : Screen("apoyos")
    object ApoyoDetalle : Screen("apoyo/{id}")
    object MisApoyos : Screen("misApoyos")

    // ----------------------
    // SOLICITUDES
    // ----------------------
    object Solicitudes : Screen("solicitudes")
}
