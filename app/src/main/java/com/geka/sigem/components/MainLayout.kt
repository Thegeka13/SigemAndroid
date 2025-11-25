package com.geka.sigem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    title: String,
    // Callbacks de navegación generales
    onNavigateToSolicitudes: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToCursos: () -> Unit,
    onNavigateToApoyos: () -> Unit,
    onLogout: () -> Unit,
    // Opcional: Acciones específicas de la barra superior (ej. botón "+" en marketplace)
    topBarActions: @Composable RowScope.() -> Unit = {},
    // El contenido de la pantalla específica
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onMarket = {
                    scope.launch { drawerState.close() }
                    onNavigateToMarket()
                },
                onCursos = {
                    scope.launch { drawerState.close() }
                    onNavigateToCursos()
                },
                onApoyos = {
                    scope.launch { drawerState.close() }
                    onNavigateToApoyos()
                },
                onSolicitudes = {
                    scope.launch { drawerState.close() }
                    onNavigateToSolicitudes()
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = topBarActions // Aquí inyectamos los botones extra si existen
                )
            }
        ) { innerPadding ->
            // Aquí se renderiza tu pantalla (Marketplace, Home, etc.)
            content(innerPadding)
        }
    }
}