package com.geka.sigem

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
import com.geka.sigem.ui.inscritos.ApoyosInscritosScreen
import com.geka.sigem.ui.viewmodel.AuthViewModel
import com.geka.sigem.viewmodel.MarketplaceViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(authViewModel: AuthViewModel) {


    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estados de autenticación
    val loginResponse by authViewModel.loginState.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val realUserId = authViewModel.getUsuarioId()

    // Detectar ruta actual para cambiar el Título y mostrar/ocultar el menú
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Lógica para saber si debemos mostrar la Navbar (No mostrar en Login)
    val showBars = currentRoute != Screen.Login.route && isLoggedIn == true

    // Título dinámico según la pantalla
    val tituloApp = when {
        currentRoute == Screen.Home.route -> "Inicio"
        currentRoute?.startsWith("market") == true -> "Marketplace"
        currentRoute == Screen.Solicitudes.route -> "Solicitudes"
        currentRoute == Screen.Cursos.route -> "Cursos"
        else -> "Sigem App"
    }

    if (isLoggedIn == null) return

    val startDestination = if (isLoggedIn == true) Screen.Home.route else Screen.Login.route

    // --- ESTRUCTURA GLOBAL: DRAWER -> SCAFFOLD -> NAVHOST ---
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBars, // Deshabilita el gesto en el Login
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
                    onApoyos = {
                        navController.navigate(Screen.Apoyos.route) { launchSingleTop = true }
                        scope.launch { drawerState.close() }
                    },
                    onSolicitudes = {
                        navController.navigate(Screen.Solicitudes.route) { launchSingleTop = true }
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

            // AQUÍ OCURRE LA MAGIA: EL CONTENIDO CAMBIA, EL MENÚ SE QUEDA
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {

                // -------------------
                // LOGIN (Sin menú, gestionado por showBars = false)
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
                // HOME (Ahora está limpia, ya no recibe callbacks de menú)
                // -------------------
                composable(Screen.Home.route) {
                    // Nota: Asegúrate de quitar los parámetros del menú en tu archivo HomeScreen
                    // para que coincida con esta llamada simple.
                    HomeScreen(
                        onNavigateToSolicitudes = { navController.navigate(Screen.Solicitudes.route) },
                        onNavigateToMarket = { navController.navigate(Screen.Market.route) },
                        onNavigateToCursos = { navController.navigate(Screen.Cursos.route) },
                        onLogout = { /* Logout manejado globalmente arriba, pero si tu Home lo pide: */ }
                    )
                }

                // -------------------
                // MARKETPLACE: LISTA
                // -------------------
                composable(Screen.Market.route) {
                    val marketplaceViewModel: MarketplaceViewModel = hiltViewModel()
                    MarketplaceScreen(
                        viewModel = marketplaceViewModel,
                        idUsuario = realUserId,
                        isMyPosts = false,
                        onOpenDetail = { id -> navController.navigate("market/detail/$id") },
                        onOpenUpload = { navController.navigate(Screen.MarketNewPost.route) },
                        onOpenMarket = { /* Ya estamos aquí */ },
                        onOpenMyPosts = { navController.navigate(Screen.MarketMyPosts.route) }
                    )
                }

                // -------------------
                // MARKETPLACE: MIS POSTS
                // -------------------
                composable(Screen.MarketMyPosts.route) {
                    val marketplaceViewModel: MarketplaceViewModel = hiltViewModel()
                    MarketplaceScreen(
                        viewModel = marketplaceViewModel,
                        idUsuario = realUserId,
                        isMyPosts = true,
                        onOpenDetail = { id -> navController.navigate("market/detail/$id") },
                        onOpenUpload = { navController.navigate(Screen.MarketNewPost.route) },
                        onOpenMarket = { navController.navigate(Screen.Market.route) { popUpTo(Screen.Market.route) { inclusive = true } } },
                        onOpenMyPosts = { /* Ya estamos aquí */ },
                    )
                }

                // -------------------
                // MARKETPLACE: NUEVA PUBLICACIÓN
                // -------------------
                composable(Screen.MarketNewPost.route) {
                    val marketplaceViewModel: MarketplaceViewModel = hiltViewModel()
                    MarketplaceUploadScreen(
                        viewModel = marketplaceViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // -------------------
                // MARKETPLACE: DETALLE
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
                        authViewModel = authViewModel,
                        onBack = { navController.popBackStack() },
                        onEdit = { editId -> navController.navigate("market/edit/$editId") }
                    )
                }

                // -------------------
                // MARKETPLACE: EDITAR
                // -------------------
                composable(
                    route = "market/edit/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val marketplaceViewModel: MarketplaceViewModel = hiltViewModel()
                    MarketplaceEditScreen(
                        idPublicacion = id,
                        viewModel = marketplaceViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // -------------------
                // SOLICITUDES
                // -------------------
                composable(Screen.Solicitudes.route) {
                    SolicitudesScreen(idEmpleado = realUserId)
                }

                // -------------------
                // APOYOS
                // -------------------
                composable(Screen.Apoyos.route) {
                    ApoyosScreen(
                        onVerApoyo = { idApoyo ->
                            navController.navigate("apoyoDetalle/$idApoyo")
                        },
                        onVerMisApoyos = {
                            navController.navigate(Screen.MisApoyos.route)
                        }
                    )
                }

                // -------------------
                // MIS APOYOS INSCRITOS
                // -------------------
                composable(Screen.MisApoyos.route) {
                    ApoyosInscritosScreen(idUsuario = realUserId)
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
                    CursoDetalleScreen(
                        idCurso = idCurso,
                        idUsuario = realUserId,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}