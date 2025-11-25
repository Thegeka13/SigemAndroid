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
    object MisCursos : Screen("misCursos")


    // ----------------------
    // SOLICITUDES
    // ----------------------
    object Solicitudes : Screen("solicitudes")


    object Solicitudes : Screen("solicitudes")

    object CrearSolicitud : Screen("crear_solicitud")

    object Eventos : Screen("eventos")
    object EventoDetalle : Screen("eventos/detalle/{idEvento}")

}
