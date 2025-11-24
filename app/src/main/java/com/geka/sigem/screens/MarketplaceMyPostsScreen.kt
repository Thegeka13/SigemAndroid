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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.geka.sigem.viewmodel.MarketplaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceMyPostsScreen(
    viewModel: MarketplaceViewModel,
    idUsuario: Int,
    onOpenDetail: (Int) -> Unit,
    onOpenUpload: () -> Unit
) {
    val misPublicaciones by viewModel.misPublicaciones.collectAsState()

    // Cargar solo mis publicaciones del backend
    LaunchedEffect(Unit) {
        viewModel.cargarMisPublicaciones()
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

        when {
            misPublicaciones == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            misPublicaciones!!.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no tienes publicaciones")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(misPublicaciones!!) { pub ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            onClick = { onOpenDetail(pub.idPublicacion) }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {

                                val primeraFoto = pub.fotos.firstOrNull()?.link

                                if (primeraFoto != null) {
                                    AsyncImage(
                                        model = primeraFoto,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(170.dp)
                                    )
                                }

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = pub.producto,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "$${pub.precio}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
