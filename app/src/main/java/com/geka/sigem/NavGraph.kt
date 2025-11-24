package com.geka.sigem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.geka.sigem.screens.*
import com.geka.sigem.ui.viewmodel.AuthViewModel
import com.geka.sigem.viewmodel.MarketplaceViewModel

@Composable
fun AppNavHost(authViewModel: AuthViewModel) {

    val navController = rememberNavController()

    // Estados de autenticación
    val loginResponse by authViewModel.loginState.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // 1. OBTENER EL ID REAL DEL USUARIO
    // Usamos la función que acabamos de crear.
    // Si devuelve -1, usamos 0 o manejamos el error, pero ya no usamos números inventados.
    val realUserId = authViewModel.getUsuarioId()

    // Si aún se está determinando el estado, no mostramos nada (o un splash)
    if (isLoggedIn == null) return

    val startDestination =
        if (isLoggedIn == true) Screen.Home.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // -------------------
        // LOGIN
        // -------------------
        composable(Screen.Login.route) {
            LaunchedEffect(loginResponse) {
                if (loginResponse != null) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                onLogin = { usuario, pass -> authViewModel.login(usuario, pass) }
            )
        }

        // -------------------
        // HOME
        // -------------------
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSolicitudes = { navController.navigate(Screen.Solicitudes.route) },
                onNavigateToMarket = { navController.navigate(Screen.Market.route) },
                onNavigateToCursos = { navController.navigate(Screen.Cursos.route) },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // -------------------
        // MARKETPLACE: LISTA
        // -------------------
        composable(Screen.Market.route) {
            val marketplaceViewModel: MarketplaceViewModel = hiltViewModel()
            MarketplaceScreen(
                viewModel = marketplaceViewModel,
                onOpenDetail = { id -> navController.navigate("market/detail/$id") },
                onOpenUpload = { navController.navigate(Screen.MarketNewPost.route) }
            )
        }

        // -------------------
        // MARKETPLACE: NUEVA PUBLICACIÓN
        // -------------------
        composable(Screen.MarketNewPost.route) {
            val marketplaceViewModel: MarketplaceViewModel = hiltViewModel()

            // CORREGIDO: Usamos el ID real
            MarketplaceUploadScreen(
                viewModel = marketplaceViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // -------------------
        // MARKETPLACE: DETALLE PUBLICACIÓN
        // -------------------
        composable("market/detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: run {
                navController.popBackStack()
                return@composable
            }
            val marketplaceViewModel: MarketplaceViewModel = hiltViewModel()
            MarketplaceDetailScreen(
                idPublicacion = id,
                viewModel = marketplaceViewModel,
                onBack = { navController.popBackStack() },
                onEdit = { editId -> navController.navigate("market/edit/$editId") }
            )
        }

        // -------------------
        // SOLICITUDES
        // -------------------
        composable(Screen.Solicitudes.route) {
            // CORREGIDO: Usamos realUserId en lugar de authViewModel.idEmpleado
            // Esto asegura que funcione incluso si recargas la app.
            SolicitudesScreen(
                idEmpleado = realUserId
            )
        }

        // -------------------
        // CURSOS
        // -------------------
        composable(Screen.Cursos.route) {
            CursosScreen(
                onVerCurso = { idCurso -> navController.navigate("cursoDetalle/$idCurso") }
            )
        }

        // -------------------
        // DETALLE CURSO
        // -------------------
        composable("cursoDetalle/{idCurso}") { backStackEntry ->
            val idCurso = backStackEntry.arguments?.getString("idCurso")?.toIntOrNull() ?: run {
                navController.popBackStack()
                return@composable
            }
            // CORREGIDO: Usamos el ID real
            CursoDetalleScreen(
                idCurso = idCurso,
                idUsuario = realUserId, // <--- Antes decía 6, ahora es el ID real
                onBack = { navController.popBackStack() }
            )
        }
    }
}