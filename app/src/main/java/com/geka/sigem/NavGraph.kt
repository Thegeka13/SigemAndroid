// AppNavHost.kt
package com.geka.sigem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.geka.sigem.components.AppDrawer
import com.geka.sigem.screens.*
import com.geka.sigem.ui.viewmodel.AuthViewModel
import com.geka.sigem.Screen
import com.geka.sigem.viewmodel.EventoViewModel
import com.geka.sigem.viewmodel.MarketplaceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(authViewModel: AuthViewModel) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val loginResponse by authViewModel.loginState.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val realUserId = authViewModel.getUsuarioId()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBars = currentRoute != Screen.Login.route && isLoggedIn == true
    val loginError by authViewModel.loginError.collectAsState()

    val tituloApp = when {
        currentRoute == Screen.Home.route -> "Inicio"
        currentRoute?.startsWith("market") == true -> "Marketplace"
        currentRoute == Screen.Solicitudes.route -> "Solicitudes"
        currentRoute == Screen.Cursos.route -> "Cursos"
        currentRoute == Screen.Eventos.route -> "Eventos"
        else -> "Sigem App"
    }

    if (isLoggedIn == null) return

    val startDestination = if (isLoggedIn == true) Screen.Home.route else Screen.Login.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBars,
        drawerContent = {
            if (showBars) {
                AppDrawer(
                    onMarket = {
                        navController.navigate(Screen.Market.route) { launchSingleTop = true }
                        scope.launch { drawerState.close() }
                    },
                    onCursos = {
                        navController.navigate(Screen.Cursos.route) { launchSingleTop = true }
                        scope.launch { drawerState.close() }
                    },
                    onSolicitudes = {
                        navController.navigate(Screen.Solicitudes.route) { launchSingleTop = true }
                        scope.launch { drawerState.close() }
                    },
                    onEventos = {
                        navController.navigate(Screen.Eventos.route) { launchSingleTop = true }
                        scope.launch { drawerState.close() }
                    },
                    onApoyos = {
                        navController.navigate(Screen.Apoyos.route) { launchSingleTop = true }
                        scope.launch { drawerState.close() }
                    },
                    onLogout = {
                        scope.launch { drawerState.close() }
                        authViewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showBars) {
                    TopAppBar(
                        title = { Text(tituloApp) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú")
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {

                // -------------------
                // LOGIN (Sin menú, gestionado por showBars = false)
                // -------------------
                composable(Screen.Login.route) {

                    // Observamos el state del ViewModel
                    val loginResponse by authViewModel.loginState.collectAsState()
                    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                    // Si el login fue exitoso → navegar al Home
                    LaunchedEffect(isLoggedIn) {
                        if (isLoggedIn == true && loginResponse != null) {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }

                    // Pantalla de Login
                    LoginScreen(
                        errorMessage = loginError,
                        onLogin = { usuario, pass -> authViewModel.login(usuario, pass) }

                    )

                }

                // HOME
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToSolicitudes = { navController.navigate(Screen.Solicitudes.route) },
                        onNavigateToMarket = { navController.navigate(Screen.Market.route) },
                        onNavigateToCursos = { navController.navigate(Screen.Cursos.route) },
                        onLogout = {}
                    )
                }

                // MARKET
                composable(Screen.Market.route) {
                    val vm: MarketplaceViewModel = hiltViewModel()
                    MarketplaceScreen(
                        viewModel = vm,
                        idUsuario = realUserId,
                        isMyPosts = false,
                        onOpenDetail = { id -> navController.navigate("market/detail/$id") },
                        onOpenUpload = { navController.navigate(Screen.MarketNewPost.route) },
                        onOpenMarket = {},
                        onOpenMyPosts = { navController.navigate(Screen.MarketMyPosts.route) }
                    )
                }

                composable(Screen.MarketMyPosts.route) {
                    val vm: MarketplaceViewModel = hiltViewModel()
                    MarketplaceScreen(
                        viewModel = vm,
                        idUsuario = realUserId,
                        isMyPosts = true,
                        onOpenDetail = { id -> navController.navigate("market/detail/$id") },
                        onOpenUpload = { navController.navigate(Screen.MarketNewPost.route) },
                        onOpenMarket = {
                            navController.navigate(Screen.Market.route) {
                                popUpTo(Screen.Market.route) { inclusive = true }
                            }
                        },
                        onOpenMyPosts = {}
                    )
                }

                composable(Screen.MarketNewPost.route) {
                    val vm: MarketplaceViewModel = hiltViewModel()
                    MarketplaceUploadScreen(
                        viewModel = vm,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("market/detail/{id}") { back ->
                    val id = back.arguments?.getString("id")?.toIntOrNull() ?: return@composable
                    val vm: MarketplaceViewModel = hiltViewModel()
                    MarketplaceDetailScreen(
                        idPublicacion = id,
                        viewModel = vm,
                        authViewModel = authViewModel,
                        onBack = { navController.popBackStack() },
                        onEdit = { eid -> navController.navigate("market/edit/$eid") }
                    )
                }

                composable(
                    "market/edit/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { back ->
                    val id = back.arguments?.getInt("id") ?: return@composable
                    val vm: MarketplaceViewModel = hiltViewModel()
                    MarketplaceEditScreen(
                        idPublicacion = id,
                        viewModel = vm,
                        onBack = { navController.popBackStack() }
                    )
                }



                // -------------------
                // APOYOS
                // -------------------
                composable(Screen.Apoyos.route) {
                    ApoyosScreen(
                        onVerApoyo = { idApoyo ->
                            navController.navigate("apoyoDetalle/$idApoyo")
                        }
                    )
                }

                // -------------------
                // DETALLE APOYO
                // -------------------
                composable("apoyoDetalle/{idApoyo}") { backStackEntry ->
                    val idApoyo = backStackEntry.arguments?.getString("idApoyo")?.toIntOrNull() ?: run {
                        navController.popBackStack()
                        return@composable
                    }

                    ApoyoDetalleScreen(
                        idApoyo = idApoyo,
                        usuarioId = realUserId,
                        onBack = { navController.popBackStack() }
                    )
                }

                //Solicitudes
                composable(Screen.Solicitudes.route) {
                    val idEmpleado = authViewModel.getEmpleadoId()   // ✔ AHORA SÍ EL ID CORRECTO

                    if (idEmpleado == -1) {     // No hay sesión
                        LaunchedEffect(Unit) {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }
                    } else {
                        SolicitudesScreen(
                            idEmpleado = idEmpleado,
                            onNavigateToMarket = { navController.navigate(Screen.Market.route) },
                            onNavigateToCursos = { navController.navigate(Screen.Cursos.route) },
                            onEventos = { navController.navigate(Screen.Eventos.route) },
                            onApoyos = { navController.navigate(Screen.Apoyos.route) },
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
                    CrearSolicitudScreen(
                        idEmpleado = realUserId ?: 0,
                        onBack = { navController.popBackStack() }
                    )
                }

                // CURSOS
                composable(Screen.Cursos.route) {
                    CursosScreen(
                        onVerCurso = { idCurso -> navController.navigate("cursoDetalle/$idCurso") },
                        onMarket = { navController.navigate(Screen.Market.route) },
                        onCursos = { navController.navigate(Screen.Cursos.route) },
                        onEventos = { navController.navigate(Screen.Eventos.route) },  // ← FALTABA
                        onApoyos = { navController.navigate(Screen.Apoyos.route) },  // ← FALTABA
                        onLogout = {
                            authViewModel.logout {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        },
                        onMisCursos = { navController.navigate(Screen.MisCursos.route) }
                    )
                }

                composable(Screen.MisCursos.route) {
                    MisCursosScreen(
                        idUsuario = realUserId,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.CrearSolicitud.route) {
                    val loginState by authViewModel.loginState.collectAsState()
                    CrearSolicitudScreen(
                        idEmpleado = loginState?.idEmpleado ?: 0,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("cursoDetalle/{idCurso}") { back ->
                    val idCurso = back.arguments?.getString("idCurso")?.toIntOrNull() ?: return@composable
                    CursoDetalleScreen(
                        idCurso = idCurso,
                        idUsuario = realUserId,
                        onBack = { navController.popBackStack() }
                    )
                }

                // EVENTOS
                composable(Screen.Eventos.route) {
                    val vm: EventoViewModel = hiltViewModel()
                    EventosScreen(
                        viewModel = vm,
                        onOpenDetail = { id -> navController.navigate("eventos/detalle/$id") }
                    )
                }

                composable(
                    "eventos/detalle/{idEvento}",
                    arguments = listOf(navArgument("idEvento") { type = NavType.IntType })
                ) { back ->
                    val idEvento = back.arguments?.getInt("idEvento") ?: return@composable
                    val vm: EventoViewModel = hiltViewModel()
                    EventoDetalleScreen(
                        idEvento = idEvento,
                        viewModel = vm,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
