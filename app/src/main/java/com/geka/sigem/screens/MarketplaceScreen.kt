package com.geka.sigem.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.geka.sigem.viewmodel.MarketplaceViewModel

// Color de fondo gris muy suave para que resalten las cards blancas
val BackgroundGray = Color(0xFFF5F5F5)

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
    // CollectAsState asegura que recibimos la lista actualizada
    val todasLasPublicaciones by viewModel.publicaciones.collectAsState()

    // Variable local para el interruptor
    var mostrarMisPosts by remember { mutableStateOf(isMyPosts) }

    // --- LÓGICA DE FILTRADO ---
    val listaAMostrar = remember(todasLasPublicaciones, mostrarMisPosts, idUsuario) {
        if (mostrarMisPosts) {
            todasLasPublicaciones.filter { pub ->
                try {
                    val idDueño = pub.usuario?.id.toString()
                    val idMio = idUsuario.toString()
                    idDueño == idMio
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
        containerColor = BackgroundGray, // Fondo general gris suave
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onOpenUpload() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
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
                .fillMaxSize()
                .background(BackgroundGray)
                // --- CORRECCIÓN CRÍTICA ---
                // NO usamos .padding(padding) completo aquí porque crea el espacio blanco arriba.
                // Solo respetamos el padding de abajo para el botón flotante.
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            // --- HEADER AZUL (Estilo Cursos) ---
            HeaderMarketplace()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // --- BOTONES TIPO TABS ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón "Todo"
                    Button(
                        onClick = { mostrarMisPosts = false },
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!mostrarMisPosts) MaterialTheme.colorScheme.secondary else Color.LightGray.copy(alpha = 0.5f),
                            contentColor = if (!mostrarMisPosts) Color.White else Color.Black
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (!mostrarMisPosts) 4.dp else 0.dp)
                    ) {
                        Text(
                            "Todo",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Botón "Mis ventas"
                    Button(
                        onClick = { mostrarMisPosts = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (mostrarMisPosts) MaterialTheme.colorScheme.secondary else Color.LightGray.copy(alpha = 0.5f),
                            contentColor = if (mostrarMisPosts) Color.White else Color.Black
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (mostrarMisPosts) 4.dp else 0.dp)
                    ) {
                        Text(
                            "Mis ventas",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // --- CONTENIDO: GRID o EMPTY STATE ---
                Box(modifier = Modifier.weight(1f)) {
                    when {
                        listaAMostrar.isEmpty() -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingBag,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = if (mostrarMisPosts) "No tienes publicaciones aún" else "No hay publicaciones disponibles",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        else -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 80.dp), // Espacio extra al final para el FAB
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(listaAMostrar) { pub ->
                                    MarketItemCard(
                                        pub = pub,
                                        idUsuario = idUsuario,
                                        onOpenDetail = onOpenDetail
                                    )
                                }
                                // Spacer seguro al final
                                item(span = { GridItemSpan(2) }) {
                                    Spacer(Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTE TARJETA OPTIMIZADO ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketItemCard(
    pub: com.geka.sigem.data.models.Publicacion,
    idUsuario: Int,
    onOpenDetail: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp) // Altura fija
            .clip(RoundedCornerShape(16.dp)),
        onClick = { onOpenDetail(pub.idPublicacion) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Tarjeta blanca pura
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. IMAGEN (Toma todo el espacio disponible con weight 1f)
            val primeraFoto = pub.fotos.firstOrNull()?.link
            Box(
                modifier = Modifier
                    .weight(1f) // Esto empuja el texto hacia abajo eliminando huecos
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.2f))
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
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            }

            // 2. TEXTO (Solo ocupa lo necesario, sin weight)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Info principal
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = pub.producto,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "$${pub.precio}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp
                        )
                    }

                    // Badge "Mío" si es necesario
                    val postOwnerId = pub.usuario?.id
                    if (postOwnerId.toString() == idUsuario.toString()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = "Mío",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- HEADER ESTILO CURSOS ---
@Composable
private fun HeaderMarketplace() {
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Marketplace",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Compra y vende en tu comunidad",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}