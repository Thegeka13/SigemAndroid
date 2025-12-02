package com.geka.sigem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geka.sigem.components.EventoCard
import com.geka.sigem.viewmodel.EventoViewModel

// Color de fondo gris suave consistente con las otras pantallas
private val BackgroundGrayy = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventosScreen(
    viewModel: EventoViewModel,
    onOpenDetail: (Int) -> Unit
) {
    val eventos = viewModel.eventos
    val loading = viewModel.loading

    LaunchedEffect(Unit) {
        viewModel.cargarEventos()
    }

    Scaffold(
        containerColor = BackgroundGray
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                // Importante: Solo aplicamos padding abajo para no crear espacio blanco arriba del header
                .padding(bottom = padding.calculateBottomPadding())
        ) {

            // --- HEADER ESTILO CURSOS/MARKETPLACE ---
            HeaderEventos()

            // --- CONTENIDO ---
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f) // Ocupa el resto del espacio
            ) {
                when {
                    loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    eventos == null -> Unit // O podrías poner un estado de error/vacío aquí

                    eventos.isEmpty() -> {
                        // Estado vacío opcional (visual)
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.EventNote, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("No hay eventos próximos", color = Color.Gray)
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(eventos) { evento ->
                                // Asumo que EventoCard ya tiene su propio estilo de tarjeta.
                                // Si necesitas que le ponga fondo blanco a la tarjeta avísame,
                                // pero normalmente los componentes Card ya lo traen.
                                EventoCard(evento = evento) {
                                    onOpenDetail(evento.idEvento)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- HEADER PERSONALIZADO PARA EVENTOS ---
@Composable
private fun HeaderEventos() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 28.dp, horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Eventos",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Actividades, noticias y calendario",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}