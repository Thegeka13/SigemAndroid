package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.geka.sigem.viewmodel.MarketplaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    viewModel: MarketplaceViewModel,
    idUsuario: Int,
    isMyPosts: Boolean = false,
    onOpenDetail: (Int) -> Unit,
    onOpenUpload: () -> Unit,
    onOpenMarket: () -> Unit,
    onOpenMyPosts: () -> Unit
) {
    val todasLasPublicaciones by viewModel.publicaciones.collectAsState()

    // Lógica de Filtrado reactiva y SEGURA
    val listaAMostrar = remember(todasLasPublicaciones, isMyPosts, idUsuario) {
        if (isMyPosts) {
            todasLasPublicaciones?.filter { pub ->
                try {
                    pub.usuario.id == idUsuario
                } catch (e: Exception) {
                    false
                }
            }
        } else {
            todasLasPublicaciones
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cargarPublicaciones()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onOpenUpload() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear publicación"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // --- BOTONES DE NAVEGACIÓN ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { if (isMyPosts) onOpenMarket() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isMyPosts) Color.LightGray else MaterialTheme.colorScheme.primary,
                        contentColor = if (!isMyPosts) Color.Black else Color.White
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("Publicaciones", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }

                Button(
                    onClick = { if (!isMyPosts) onOpenMyPosts() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMyPosts) Color.LightGray else MaterialTheme.colorScheme.primary,
                        contentColor = if (isMyPosts) Color.Black else Color.White
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("Subidas", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }

            // --- LISTA DE PUBLICACIONES ---
            Box(modifier = Modifier.weight(1f)) {
                when {
                    todasLasPublicaciones == null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    listaAMostrar.isNullOrEmpty() -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isMyPosts) "No tienes publicaciones aún" else "No hay publicaciones disponibles",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(listaAMostrar!!) { pub ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp),
                                    onClick = { onOpenDetail(pub.idPublicacion) },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(modifier = Modifier.fillMaxSize()) {

                                        // Imagen
                                        val primeraFoto = pub.fotos.firstOrNull()?.link
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            if (primeraFoto != null) {
                                                AsyncImage(
                                                    model = primeraFoto,
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            } else {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text("Sin imagen", style = MaterialTheme.typography.bodySmall)
                                                }
                                            }
                                        }

                                        // Info
                                        Column(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth()
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

                                            // Acceso seguro al ID del usuario
                                            val postOwnerId = try {
                                                pub.usuario.id
                                            } catch (e: Exception) {
                                                -1
                                            }

                                            if (postOwnerId == idUsuario) {
                                                Spacer(Modifier.height(8.dp))
                                                Surface(
                                                    shape = MaterialTheme.shapes.small,
                                                    color = MaterialTheme.colorScheme.secondaryContainer
                                                ) {
                                                    Text(
                                                        text = "Tu publicación",
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                Spacer(Modifier.height(80.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}