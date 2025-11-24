package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.geka.sigem.ui.viewmodel.SolicitudViewModel

@Composable
fun CrearSolicitudScreen(
    idEmpleado: Int,
    viewModel: SolicitudViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }


    Column(modifier = Modifier.padding(20.dp)) {

        Text("Nueva Solicitud", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = fechaInicio,
            onValueChange = { fechaInicio = it },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            label = { Text("Fecha Inicio (YYYY-MM-DD)") }
        )

        OutlinedTextField(
            value = fechaFin,
            onValueChange = { fechaFin = it },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            label = { Text("Fecha Fin (YYYY-MM-DD)") }
        )

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            label = { Text("Motivo de la solicitud") }
        )

        Button(
            onClick = {
                viewModel.crearSolicitud(
                    idEmpleado,
                    fechaInicio,
                    fechaFin,
                    motivo
                ) {
                    onBack()   // ← se ejecuta ENSAMBLE al terminar
                }
            },
            modifier = Modifier.padding(top = 22.dp)
        ) {
            Text("Enviar Solicitud")
        }

    }
}
