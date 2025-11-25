package com.geka.sigem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geka.sigem.data.models.Apoyo
import com.geka.sigem.ui.viewmodel.ApoyosViewModel

// Colores basados en tu imagen
val AzulPrimario = Color(0xFF0D5C96)
val AzulClaro = Color(0xFF1976D2)
val GrisClaro = Color(0xFFF5F5F5)
val GrisTexto = Color(0xFF666666)
val VerdeCheck = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApoyosScreen(
    onVerApoyo: (Int) -> Unit,
    viewModel: ApoyosViewModel = viewModel()
) {
    val apoyos by viewModel.apoyos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadApoyos()
    }

    Scaffold(
        containerColor = GrisClaro
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header con fondo azul
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                color = AzulPrimario
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Apoyos",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Explora los apoyos disponibles",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Contenido
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulPrimario)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(apoyos) { apoyo ->
                        ApoyoCard(apoyo = apoyo, onVerApoyo = onVerApoyo)
                    }
                }
            }

            message?.let {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun ApoyoCard(apoyo: Apoyo, onVerApoyo: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Nombre del apoyo
            Text(
                text = apoyo.nombre ?: "Sin nombre",
                style = MaterialTheme.typography.titleLarge,
                color = AzulPrimario,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción con icono
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = AzulClaro,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.labelSmall,
                        color = GrisTexto,
                        fontSize = 11.sp
                    )
                    Text(
                        text = apoyo.descripcion ?: "Sin descripción disponible",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Requisitos con icono
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = VerdeCheck,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Requisitos",
                        style = MaterialTheme.typography.labelSmall,
                        color = GrisTexto,
                        fontSize = 11.sp
                    )
                    Text(
                        text = apoyo.requisitos ?: "No especificados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Línea separadora
            Divider(
                color = GrisClaro,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Progreso visual (opcional, puedes ajustar según tus datos)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cupos disponibles",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "14/47",
                    style = MaterialTheme.typography.bodySmall,
                    color = AzulClaro,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Barra de progreso
            LinearProgressIndicator(
                progress = 0.3f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AzulClaro,
                trackColor = GrisClaro
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Ver detalles"
            Button(
                onClick = { onVerApoyo(apoyo.idApoyo ?: 0) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPrimario
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Ver detalles",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}