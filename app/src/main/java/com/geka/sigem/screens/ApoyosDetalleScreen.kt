package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.geka.sigem.ui.viewmodel.ApoyosViewModel

// Colores
val AzulPrimario1 = Color(0xFF0D5C96)
val AzulClaro1 = Color(0xFF1976D2)
val GrisClaro1 = Color(0xFFF5F5F5)
val GrisTexto1 = Color(0xFF666666)
val VerdeCheck1 = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApoyoDetalleScreen(
    idApoyo: Int,
    usuarioId: Int,
    onBack: () -> Unit,
    viewModel: ApoyosViewModel = viewModel()
) {
    val apoyo by viewModel.apoyo.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(idApoyo) {
        viewModel.loadApoyo(idApoyo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle del Apoyo",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulPrimario1
                )
            )
        },
        containerColor = GrisClaro1
    ) { innerPadding ->

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AzulPrimario1)
            }
            return@Scaffold
        }

        apoyo?.let { a ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header con el nombre del apoyo
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = AzulPrimario1,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = a.nombre ?: "Sin nombre",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AzulPrimario1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Sección de Descripción
                InfoSection(
                    icon = Icons.Default.Description,
                    title = "Descripción",
                    content = a.descripcion ?: "Sin descripción disponible"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Sección de Requisitos
                InfoSection(
                    icon = Icons.Default.CheckCircle,
                    title = "Requisitos",
                    content = a.requisitos ?: "No especificados",
                    iconTint = VerdeCheck1
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Información adicional
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Cupos disponibles",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrisTexto1,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "14/47",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = AzulClaro1,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = AzulClaro1,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = 0.3f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AzulClaro1,
                            trackColor = GrisClaro1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de Inscripción
                Button(
                    onClick = {
                        viewModel.inscribirEnApoyo(
                            idUsuario = usuarioId,
                            idApoyo = a.idApoyo!!
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrimario1
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Inscribirme a este apoyo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mensaje de éxito o error
                message?.let { msg ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = if (msg.contains("éxito", ignoreCase = true))
                            Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (msg.contains("éxito", ignoreCase = true))
                                    Icons.Default.CheckCircle else Icons.Default.Error,
                                contentDescription = null,
                                tint = if (msg.contains("éxito", ignoreCase = true))
                                    VerdeCheck1 else Color(0xFFD32F2F),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = msg,
                                color = if (msg.contains("éxito", ignoreCase = true))
                                    Color(0xFF2E7D32) else Color(0xFFC62828),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = { viewModel.clearMessage() },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text("Cerrar mensaje", color = AzulClaro1)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = GrisTexto1,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message ?: "Apoyo no encontrado",
                    color = GrisTexto1,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(onClick = onBack) {
                    Text("Volver", color = AzulClaro1)
                }
            }
        }
    }
}

@Composable
fun InfoSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    iconTint: Color = AzulClaro1
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = AzulPrimario1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}