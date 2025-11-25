package com.geka.sigem.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.geka.sigem.viewmodel.EventoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoDetalleScreen(
    idEvento: Int,
    viewModel: EventoViewModel,
    onBack: () -> Unit
) {
    val evento = viewModel.evento
    val loading = viewModel.loading

    LaunchedEffect(idEvento) {
        viewModel.cargarEvento(idEvento)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        if (loading || evento == null) {
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            AsyncImage(
                model = evento.bannerLink,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )

            Column(Modifier.padding(16.dp)) {

                Text(evento.nombre, style = MaterialTheme.typography.titleLarge)

                Text(
                    text = "Fecha ${evento.fechaInicio.substring(0,10)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(24.dp))

                val userConfirmed = evento.confirmaciones.any { it.confirmado == "1" }

                Button(
                    onClick = {
                        if (!userConfirmed)
                            viewModel.confirmarAsistencia(idEvento, 1)
                        else {
                            val id = evento.confirmaciones.first().idConfirmacion
                            viewModel.desconfirmar(id, idEvento)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (!userConfirmed) "Asistir"
                        else "Desconfirmar"
                    )
                }
            }
        }
    }
}
