package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.geka.sigem.viewmodel.MarketplaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceDetailScreen(
    idPublicacion: Int,
    viewModel: MarketplaceViewModel,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit
) {
    val detalle by viewModel.detalle.collectAsState()

    // Cargar detalle
    LaunchedEffect(idPublicacion) {
        viewModel.cargarPublicacion(idPublicacion)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(idPublicacion) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar publicación")
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

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Carrusel simple de fotos
            if (pub.fotos.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pub.fotos) { foto ->
                        AsyncImage(
                            model = foto.link,
                            contentDescription = null,
                            modifier = Modifier
                                .height(180.dp)
                                .width(250.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            Text(
                text = pub.producto,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "$${pub.precio}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            Text("Descripción", style = MaterialTheme.typography.titleSmall)
            Text(pub.descripcion)

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { /* TODO: CHAT / CONTACTO */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contactar vendedor")
            }
        }
    }
}
