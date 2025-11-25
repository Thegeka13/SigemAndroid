package com.geka.sigem

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Market : Screen("market")
    object Cursos: Screen ("cursos")
    object CursoDetalle: Screen ("curso/{id}")

    object Solicitudes : Screen("solicitudes")

    object CrearSolicitud : Screen("crear_solicitud")

    object ChangeCredentials : Screen("change_credentials")


}
