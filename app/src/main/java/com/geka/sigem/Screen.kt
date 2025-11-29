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
    // SPLASH
    // ----------------------
    object Splash : Screen("splash")

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
    object CursoDetalle : Screen("cursoDetalle/{idCurso}")
    object MisCursos : Screen("misCursos")

    // ----------------------
    // APOYOS
    // ----------------------
    object Apoyos : Screen("apoyos")
    object ApoyoDetalle : Screen("apoyoDetalle/{idApoyo}")

    // ----------------------
    // SOLICITUDES
    // ----------------------
    object Solicitudes : Screen("solicitudes")
    object CrearSolicitud : Screen("crear_solicitud")

    object ChangeCredentialsScreen : Screen("change_credentials")

    // ----------------------
    // EVENTOS
    // ----------------------
    object Eventos : Screen("eventos")
    object EventoDetalle : Screen("eventos/detalle/{idEvento}")

    // ----------------------
    // IA
    // ----------------------
    object AiHelpScreen : Screen("ai_help")
}
