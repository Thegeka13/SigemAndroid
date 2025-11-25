package com.geka.sigem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    title: String,

    // Callbacks generales del menú
    onNavigateToSolicitudes: () -> Unit,
    onNavigateToMarket: () -> Unit,
    onNavigateToCursos: () -> Unit,
    onNavigateToApoyos: () -> Unit,
    onNavigateToEventos: () -> Unit,   // ← AGREGADO
    onLogout: () -> Unit,

    // Acciones de la topbar (opcional)
    topBarActions: @Composable RowScope.() -> Unit = {},

    // Contenido de la pantalla
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
                onEventos = {                    // ← CORREGIDO
                    scope.launch { drawerState.close() }
                    onNavigateToEventos()
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
                    actions = topBarActions
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
