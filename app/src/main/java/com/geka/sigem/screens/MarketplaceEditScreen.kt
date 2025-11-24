package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.geka.sigem.data.remote.uriToFile
import com.geka.sigem.viewmodel.MarketplaceViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceEditScreen(
    idPublicacion: Int,
    viewModel: MarketplaceViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val detalle by viewModel.detalle.collectAsState()

    var producto by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val fotosAEliminar = remember { mutableStateListOf<Int>() }
    val nuevasUris = remember { mutableStateListOf<Uri>() }

    // Cargar detalle al abrir
    LaunchedEffect(idPublicacion) {
        viewModel.cargarPublicacion(idPublicacion)
    }

    // Llenar campos cuando llega la data
    LaunchedEffect(detalle) {
        detalle?.let { p ->
            producto = p.producto
            precio = p.precio
            descripcion = p.descripcion
        }
    }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        nuevasUris.addAll(uris)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar publicación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
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

            OutlinedTextField(
                value = producto,
                onValueChange = { producto = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Producto") }
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Precio") }
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descripción") },
                maxLines = 5
            )

            Spacer(Modifier.height(16.dp))

            Text("Fotos actuales")

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(pub.fotos) { foto ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        AsyncImage(
                            model = foto.link,
                            contentDescription = null,
                            modifier = Modifier
                                .height(110.dp)
                                .width(140.dp)
                        )

                        TextButton(onClick = {
                            fotosAEliminar.add(foto.idFoto)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Text("Eliminar")
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { picker.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar nuevas fotos")
            }

            // Previsualización de nuevas imágenes
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(nuevasUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .height(110.dp)
                            .width(140.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val nuevosFiles = nuevasUris.map { uriToFile(context, it) }

                    viewModel.actualizarPublicacion(
                        id = idPublicacion,
                        producto = producto,
                        precio = precio,
                        descripcion = descripcion,
                        estatus = 1,
                        fotosAEliminar = fotosAEliminar.toList(),
                        nuevasImagenes = nuevosFiles,
                        onFinish = { success ->
                            if (success) onBack()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}
