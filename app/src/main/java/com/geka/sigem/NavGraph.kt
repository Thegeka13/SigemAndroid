package com.geka.sigem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.geka.sigem.screens.*
import com.geka.sigem.ui.viewmodel.AuthViewModel
import com.geka.sigem.Screen
import com.geka.sigem.screens.CrearSolicitudScreen

@Composable
fun AppNavHost(authViewModel: AuthViewModel) {

    val navController = rememberNavController()

    // Flujo del login
    val loginResponse by authViewModel.loginState.collectAsState()

    // Estado de sesión
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Si null → aún cargando
    if (isLoggedIn == null) return

    val startDestination =
        if (isLoggedIn == true) Screen.Home.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {

        // -------------------------------
        // LOGIN
        // -------------------------------
        composable(Screen.Login.route) {

            LoginScreen(
                onLogin = { usuario, pass ->
                    authViewModel.login(usuario, pass)
                }
            )

            // Navegar cuando login es exitoso
            if (loginResponse != null) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }

        // -------------------------------
        // HOME
        // -------------------------------
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSolicitudes = {
                    navController.navigate(Screen.Solicitudes.route)
                },
                onNavigateToMarket = {
                    navController.navigate(Screen.Market.route)
                },
                onNavigateToCursos = {
                    navController.navigate(Screen.Cursos.route)
                },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        }

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


        // -------------------------------
        // MARKET
        // -------------------------------
        composable(Screen.Market.route) {
            MarketScreen(onBack = { navController.popBackStack() })
        }

        //--------------------------------
        // SOLICITUDES
        //--------------------------------

        composable(Screen.Solicitudes.route) {
            val id = authViewModel.idEmpleado
            if (id == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            } else {
                SolicitudesScreen(
                    idEmpleado = authViewModel.idEmpleado!!,
                    onNavigateToMarket = { navController.navigate(Screen.Market.route) },
                    onNavigateToCursos = { navController.navigate(Screen.Cursos.route) },
                    onLogout = {
                        authViewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    },
                    onCrearSolicitud = {
                        navController.navigate(Screen.CrearSolicitud.route)
                    }
                )
            }
        }

        composable(Screen.CrearSolicitud.route) {

            val loginState by authViewModel.loginState.collectAsState()

            CrearSolicitudScreen(
                idEmpleado = loginState?.idEmpleado ?: 0,
                onBack = { navController.popBackStack() }
            )
        }

        // -------------------------------
        // CURSOS
        // -------------------------------
        composable(Screen.Cursos.route) {
            CursosScreen(
                onVerCurso = { idCurso ->
                    navController.navigate("cursoDetalle/$idCurso")
                }
            )
        }

        // -------------------------------
        // DETALLE CURSO
        // -------------------------------
        composable("cursoDetalle/{idCurso}") { backStackEntry ->
            val idCurso = backStackEntry.arguments?.getString("idCurso")!!.toInt()

            CursoDetalleScreen(
                idCurso = idCurso,
                idUsuario = 6,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
