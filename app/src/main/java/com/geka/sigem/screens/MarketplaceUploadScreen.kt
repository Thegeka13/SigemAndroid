package com.geka.sigem.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.geka.sigem.data.remote.uriToFile
import com.geka.sigem.viewmodel.MarketplaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceUploadScreen(
    viewModel: MarketplaceViewModel,
    // idUsuario: Int, // <-- ELIMINADO: Ya no se necesita aquí
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Estados del formulario
    var producto by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Estado de carga para bloquear el botón (opcional pero recomendado)
    var isUploading by remember { mutableStateOf(false) }

    val imagenesUris = remember { mutableStateListOf<Uri>() }

    // Picker de imágenes
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            imagenesUris.addAll(uris)
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Subir publicación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = producto,
                onValueChange = { producto = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Producto") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Precio") },
                singleLine = true
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

            Button(
                onClick = { picker.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar imágenes (${imagenesUris.size})")
            }

            Spacer(Modifier.height(12.dp))

            // Previsualización de imágenes seleccionadas
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(imagenesUris) { uri ->
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
                enabled = !isUploading && producto.isNotEmpty() && precio.isNotEmpty(),
                onClick = {
                    isUploading = true
                    // Convertir URIs a Files
                    val imagenesFiles = imagenesUris.map { uriToFile(context, it) }

                    // Llamada al ViewModel (sin pasar ID)
                    viewModel.crearPublicacion(
                        producto = producto,
                        precio = precio,
                        descripcion = descripcion,
                        imagenes = imagenesFiles,
                        onFinish = { success ->
                            isUploading = false
                            if (success) {
                                Toast.makeText(context, "Publicación creada con éxito", Toast.LENGTH_SHORT).show()
                                onBack()
                            } else {
                                Toast.makeText(context, "Error al crear publicación", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Subir publicación")
                }
            }
        }
    }
}