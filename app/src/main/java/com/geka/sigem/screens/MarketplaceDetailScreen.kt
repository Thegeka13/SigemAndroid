package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.geka.sigem.ui.viewmodel.AuthViewModel
import com.geka.sigem.viewmodel.MarketplaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceDetailScreen(
    idPublicacion: Int,
    viewModel: MarketplaceViewModel,
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit
) {
    val detalle by viewModel.detalle.collectAsState()

    // Obtenemos el ID del usuario actual
    val currentUserId = remember { authViewModel.getUsuarioId() }

    LaunchedEffect(idPublicacion) {
        viewModel.cargarPublicacion(idPublicacion)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    val pub = detalle
                    // 1. COMPARACIÓN CORRECTA:
                    // Verificamos si pub no es null y si el ID del usuario coincide
                    if (pub != null && pub.usuario.id == currentUserId) {
                        IconButton(onClick = { onEdit(idPublicacion) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar publicación")
                        }
                    }
                }
            )
        }
    ) { padding ->

        val pub = detalle

        if (pub == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // Variable para saber si soy el dueño (para usar en el UI)
        val isOwner = (pub.usuario.id == currentUserId)

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // --- CARRUSEL DE FOTOS ---
            if (pub.fotos.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pub.fotos) { foto ->
                        AsyncImage(
                            model = foto.link,
                            contentDescription = null,
                            modifier = Modifier
                                .height(200.dp)
                                .width(280.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // --- INFO DEL PRODUCTO ---
            Text(
                text = pub.producto,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "$${pub.precio}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(16.dp))

            Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                text = pub.descripcion,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(Modifier.height(24.dp))

            // --- INFO DEL VENDEDOR O PANEL DE DUEÑO ---
            if (isOwner) {
                // Si soy el dueño, mostramos un aviso simple
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Padding(16.dp) {
                        Text(
                            "Esta es tu publicación",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            "Puedes editarla desde el icono en la parte superior.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                // Si NO soy el dueño, mostramos los datos del empleado
                Text(
                    text = "Datos del Vendedor",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val emp = pub.usuario.empleado

                        // Nombre
                        InfoRow(
                            icon = Icons.Default.Person,
                            label = "Nombre",
                            value = "${emp.nombre} ${emp.apellido}"
                        )

                        // Correo (Mostramos el institucional, o el personal si prefieres)
                        InfoRow(
                            icon = Icons.Default.Email,
                            label = "Correo",
                            value = emp.correoInstitucional // o emp.correoPersonal
                        )

                        // Teléfono
                        InfoRow(
                            icon = Icons.Default.Phone,
                            label = "Teléfono",
                            value = emp.telefonoPersonal
                        )

                        // Extensión
                        if (!emp.extension.isNullOrEmpty()) {
                            InfoRow(
                                icon = Icons.Default.Call, // Icono alternativo para ext
                                label = "Extensión",
                                value = emp.extension
                            )
                        }

                        // Departamento / Ubicación (Usando 'Colonia' del JSON)
                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            label = "Ubicación/Depto",
                            value = emp.colonia
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// Composable auxiliar para hacer las filas de información más limpias
@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Helper pequeño para padding
@Composable
fun Padding(all: androidx.compose.ui.unit.Dp, content: @Composable () -> Unit) {
    Box(Modifier.padding(all)) { content() }
}