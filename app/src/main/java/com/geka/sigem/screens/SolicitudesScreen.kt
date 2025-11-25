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

@Composable
fun SolicitudesScreen(
    idEmpleado: Int,                     // recomendado al inicio
    onNavigateToMarket: () -> Unit,
    onNavigateToCursos: () -> Unit,
    onEventos: () -> Unit,               // este parámetro SÍ debe existir
    onLogout: () -> Unit,
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
                .background(Color(0xFFF5F5F5))
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mis Solicitudes",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Gestión de vacaciones",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.weight(1f)) {

                if (solicitudes.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(1.dp)
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
                                tint = Color(0xFFE0E0E0)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No hay solicitudes",
                                fontSize = 16.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(solicitudes) { solicitud ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(1.dp)
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
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${solicitud.fechaInicio} - ${solicitud.fechaFin}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF1A1A1A)
                                        )

                                        Text(
                                            text = "Solicitud de vacaciones",
                                            fontSize = 14.sp,
                                            color = Color(0xFF6B7280),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )

                                        if (!solicitud.comentarioAdministrador.isNullOrEmpty()) {
                                            Text(
                                                text = solicitud.comentarioAdministrador!!,
                                                fontSize = 13.sp,
                                                color = Color(0xFF6B7280),
                                                modifier = Modifier.padding(top = 6.dp)
                                            )
                                        }

                                        Text(
                                            text = solicitud.estado,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                shape = RoundedCornerShape(8.dp),
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
