package com.geka.sigem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.geka.sigem.ui.viewmodel.AuthViewModel
import com.geka.sigem.viewmodel.MarketplaceViewModel

// El mismo gris de fondo para mantener consistencia
private val BackgroundGrayy = Color(0xFFF5F5F5)

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

    // Calculamos si es dueño fuera del Scaffold para usarlo en el FAB
    val pub = detalle
    val isOwner = if (pub != null) (pub.usuario.id == currentUserId) else false

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            HeaderDetailMarketplace(onBack = onBack)
        },
        // BOTÓN FLOTANTE: Solo aparece si eres el dueño para Editar
        floatingActionButton = {
            if (isOwner) {
                FloatingActionButton(
                    onClick = { onEdit(idPublicacion) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar publicación")
                }
            }
        }
    ) { padding ->

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

        Column(
            modifier = Modifier
                .padding(padding) // Respetamos el padding del header
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // --- CARRUSEL DE FOTOS ---
            // Le damos un fondo blanco al contenedor de fotos para que resalten
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 16.dp)
            ) {
                if (pub.fotos.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(pub.fotos) { foto ->
                            AsyncImage(
                                model = foto.link,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(220.dp)
                                    .width(300.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                } else {
                    // Placeholder si no hay fotos
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.ImageNotSupported, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(50.dp))
                            Text("Sin imágenes", color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- CONTENIDO PRINCIPAL ---
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // Tarjeta de Información Principal
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = pub.producto,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "$${pub.precio}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(Modifier.height(16.dp))

                        Text(
                            "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = pub.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // --- INFO DEL VENDEDOR O AVISO DE PROPIEDAD ---
                if (isOwner) {
                    // Si soy el dueño
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Esta publicación es tuya",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Usa el botón inferior para editar.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                } else {
                    // Si NO soy el dueño (Vendedor)
                    Text(
                        text = "Datos del Vendedor",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val emp = pub.usuario.empleado

                            // Nombre con Avatar simple (Icono)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "${emp.nombre} ${emp.apellido}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Vendedor",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }

                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                            // Contacto
                            InfoRow(
                                icon = Icons.Default.Email,
                                label = "Correo",
                                value = emp.correoInstitucional
                            )

                            InfoRow(
                                icon = Icons.Default.Phone,
                                label = "Teléfono",
                                value = emp.telefonoPersonal
                            )

                            if (!emp.extension.isNullOrEmpty()) {
                                InfoRow(
                                    icon = Icons.Default.Call,
                                    label = "Extensión",
                                    value = emp.extension
                                )
                            }

                            InfoRow(
                                icon = Icons.Default.LocationOn,
                                label = "Ubicación",
                                value = emp.colonia
                            )
                        }
                    }
                }

                // Espacio final para que el FAB no tape nada
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

// --- HEADER PERSONALIZADO ---
@Composable
private fun HeaderDetailMarketplace(onBack: () -> Unit) {
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
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    "Detalle del Producto",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    "Información completa",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// --- HELPER COMPOSABLE ---
@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}