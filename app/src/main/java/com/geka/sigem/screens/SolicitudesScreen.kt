package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geka.sigem.ui.viewmodel.SolicitudViewModel

@Composable
fun SolicitudesScreen(
    idEmpleado: Int,
    onCrearSolicitud: () -> Unit,
    viewModel: SolicitudViewModel = viewModel()
) {
    val solicitudes by viewModel.solicitudes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarSolicitudes(idEmpleado)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis Solicitudes de Vacaciones")

        Button(
            onClick = onCrearSolicitud,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text("Crear nueva solicitud")
        }

        solicitudes.forEach {
            Text("Del ${it.fechaInicio} al ${it.fechaFin} → ${it.estado}")
        }
    }
}

