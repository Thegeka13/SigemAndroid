package com.geka.sigem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.geka.sigem.viewmodel.EventoViewModel

private val BackgraoundGray = Color(0xFFF5F5F5)

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
        containerColor = BackgroundGray,
        topBar = {
            HeaderDetalleEvento(onBack = onBack)
        }
        // Se eliminó el floatingActionButton aquí
    ) { padding ->

        if (loading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (evento == null) return@Scaffold

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // --- BANNER DEL EVENTO ---
            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                if (!evento.bannerLink.isNullOrEmpty()) {
                    AsyncImage(
                        model = evento.bannerLink,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder si no hay imagen (Gris oscuro)
                    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
                }
            }

            // --- TARJETA DE INFORMACIÓN ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                // 1. NOMBRE
                Text(
                    text = evento.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(16.dp))

                // 2. FECHA
                val fechaSegura = evento.fechaInicio ?: ""
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        // Cortamos la fecha solo si es segura y lo suficientemente larga
                        text = if (fechaSegura.length >= 10) "Fecha: ${fechaSegura.substring(0, 10)}" else fechaSegura,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(24.dp))
                HorizontalDivider(color = BackgroundGray, thickness = 2.dp)
                Spacer(Modifier.height(24.dp))

                // 3. DESCRIPCIÓN
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))

                // Aquí usamos el operador Elvis (?:) para evitar el crash si viene null
                Text(
                    text = evento.descripcion ?: "Sin descripción disponible.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 24.sp
                )

                // Se eliminó el Spacer de 80.dp extra
            }
        }
    }
}

// Header reutilizable
@Composable
private fun HeaderDetalleEvento(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 16.dp, bottom = 24.dp, start = 8.dp, end = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Regresar", tint = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Detalle del Evento",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
    }
}