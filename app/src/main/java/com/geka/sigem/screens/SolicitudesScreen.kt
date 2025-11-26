package com.geka.sigem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geka.sigem.components.AppDrawer
import com.geka.sigem.data.models.Evento
import com.geka.sigem.ui.viewmodel.SolicitudViewModel
import kotlinx.coroutines.launch

// Paleta de colores profesional
object SolicitudesColors {
    val primaryBlue = Color(0xFF1E40AF)      // Azul profundo
    val lightBlue = Color(0xFF3B82F6)        // Azul claro
    val accentBlue = Color(0xFF0EA5E9)       // Azul brillante
    val veryLightBlue = Color(0xFFEFF6FF)    // Fondo azul muy claro
    val darkGray = Color(0xFF1F2937)
    val mediumGray = Color(0xFF6B7280)
    val lightGray = Color(0xFFF3F4F6)
}

@Composable
fun SolicitudesScreen(
    idEmpleado: Int,                     // recomendado al inicio
    onNavigateToMarket: () -> Unit,
    onNavigateToCursos: () -> Unit,
    onEventos: () -> Unit,               // este parámetro SÍ debe existir
    onLogout: () -> Unit,
    onApoyos: () -> Unit,
    onCrearSolicitud: () -> Unit,
    viewModel: SolicitudViewModel = viewModel()
) {
    val solicitudes by viewModel.solicitudes.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onMarket = {
                    onNavigateToMarket()
                    scope.launch { drawerState.close() }
                },
                onCursos = {
                    onNavigateToCursos()
                    scope.launch { drawerState.close() }
                },
                onSolicitudes = {
                    scope.launch { drawerState.close() }
                },
                onEventos = {
                    onEventos()                      // CORREGIDO
                    scope.launch { drawerState.close() }
                },
                onApoyos = {
                    onApoyos()                      // CORREGIDO
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    onLogout()
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        LaunchedEffect(Unit) {
            viewModel.cargarSolicitudes(idEmpleado)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Header con gradiente azul
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                SolicitudesColors.primaryBlue,
                                SolicitudesColors.lightBlue
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Mis Solicitudes",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Gestión de vacaciones",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
            ) {

                if (solicitudes.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SolicitudesColors.veryLightBlue
                        ),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = SolicitudesColors.lightBlue
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No hay solicitudes",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SolicitudesColors.darkGray
                            )
                        }
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(solicitudes) { solicitud ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = SolicitudesColors.lightBlue,
                                        modifier = Modifier.size(32.dp)
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${solicitud.fechaInicio} - ${solicitud.fechaFin}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SolicitudesColors.darkGray
                                        )

                                        Text(
                                            text = "Solicitud de vacaciones",
                                            fontSize = 14.sp,
                                            color = SolicitudesColors.mediumGray,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )

                                        if (!solicitud.comentarioAdministrador.isNullOrEmpty()) {
                                            Text(
                                                text = solicitud.comentarioAdministrador!!,
                                                fontSize = 13.sp,
                                                color = SolicitudesColors.mediumGray,
                                                modifier = Modifier.padding(top = 6.dp)
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .background(
                                                    color = SolicitudesColors.veryLightBlue,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = solicitud.estado,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = SolicitudesColors.primaryBlue
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCrearSolicitud,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SolicitudesColors.accentBlue),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Nueva Solicitud",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}