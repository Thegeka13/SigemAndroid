package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.geka.sigem.viewmodel.MarketplaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceMyPostsScreen(
    viewModel: MarketplaceViewModel,
    idUsuario: Int, // El ID del usuario logueado
    onOpenDetail: (Int) -> Unit,
    onOpenUpload: () -> Unit,
    onOpenMarket: () -> Unit // Necesario para el botón de "Publicaciones"
) {
    // 1. Observamos la lista GENERAL (para asegurarnos de tener data fresca)
    val todasLasPublicaciones by viewModel.publicaciones.collectAsState()

    // 2. Cargamos las publicaciones generales al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarPublicaciones()
    }

    // 3. LÓGICA DE FILTRADO:
    // Creamos una lista derivada que solo contenga los posts donde el id coincida.
    // Usamos 'remember' para que no recalcule en cada frame, solo si cambia la lista o el ID.
    val misPublicacionesFiltradas = remember(todasLasPublicaciones, idUsuario) {
        todasLasPublicaciones?.filter { pub ->
            // Asegúrate de que tu objeto Publicacion tenga el campo idUsuario
            pub.usuario.id == idUsuario
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Publicaciones") },
                actions = {
                    IconButton(onClick = onOpenUpload) {
                        Icon(Icons.Default.Add, contentDescription = "Subir")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // --- BOTONES DE NAVEGACIÓN (Consistente con la otra pantalla) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Publicaciones (Activo, color normal)
                Button(
                    onClick = onOpenMarket,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Publicaciones")
                }

                // Botón Mis Publicaciones (Inactivo visualmente -> Gris)
                Button(
                    onClick = { /* Ya estamos aquí */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray, // Gris porque estamos aquí
                        contentColor = Color.Black
                    )
                ) {
                    Text("Mis Publicaciones")
                }
            }

            // --- CONTENIDO DE LA LISTA FILTRADA ---
            Box(modifier = Modifier.weight(1f)) {
                when {
                    todasLasPublicaciones == null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    misPublicacionesFiltradas.isNullOrEmpty() -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("No tienes publicaciones activas.")
                                Spacer(Modifier.height(8.dp))
                                Text("¡Sube la primera!", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(misPublicacionesFiltradas) { pub ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp), // Diseño tipo fila horizontal
                                    onClick = { onOpenDetail(pub.idPublicacion) },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxSize()) {
                                        // Imagen izquierda
                                        val primeraFoto = pub.fotos.firstOrNull()?.link
                                        if (primeraFoto != null) {
                                            AsyncImage(
                                                model = primeraFoto,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .width(120.dp)
                                                    .fillMaxHeight(),
                                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .width(120.dp)
                                                    .fillMaxHeight()
                                                    .padding(8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("Sin foto", style = MaterialTheme.typography.bodySmall)
                                            }
                                        }

                                        // Info derecha
                                        Column(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = pub.producto,
                                                style = MaterialTheme.typography.titleMedium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(Modifier.height(4.dp))
                                            Text(
                                                text = "$${pub.precio}",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(Modifier.height(8.dp))
                                            // Etiqueta opcional "Tu publicación"
                                            Surface(
                                                shape = MaterialTheme.shapes.small,
                                                color = MaterialTheme.colorScheme.secondaryContainer
                                            ) {
                                                Text(
                                                    text = "Tu publicación",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}