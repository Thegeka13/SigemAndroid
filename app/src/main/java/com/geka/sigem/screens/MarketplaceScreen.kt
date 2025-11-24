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
fun MarketplaceScreen(
    viewModel: MarketplaceViewModel,
    onOpenDetail: (Int) -> Unit,
    onOpenUpload: () -> Unit
) {
    val publicaciones by viewModel.publicaciones.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarPublicaciones()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Marketplace") },
                actions = {
                    IconButton(onClick = onOpenUpload) {
                        Icon(Icons.Default.Add, contentDescription = "Subir")
                    }
                }
            )
        }
    ) { padding ->

        when {
            publicaciones == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            publicaciones!!.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay publicaciones disponibles")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(publicaciones!!) { pub ->

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

                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    }
}
