package com.geka.sigem.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.geka.sigem.data.remote.uriToFile
import com.geka.sigem.viewmodel.MarketplaceViewModel

// Mantenemos la consistencia del color de fondo
private val BackgroundGrayy = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceUploadScreen(
    viewModel: MarketplaceViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Estados del formulario
    var producto by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Estado de carga
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
        containerColor = BackgroundGray,
        topBar = {
            HeaderUploadMarketplace(onBack = onBack)
        },
        // Botón flotante para la acción principal (Subir)
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Validación simple antes de proceder
                    if (!isUploading && producto.isNotEmpty() && precio.isNotEmpty()) {
                        isUploading = true
                        // Convertir URIs a Files
                        val imagenesFiles = imagenesUris.map { uriToFile(context, it) }

                        // Llamada al ViewModel
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
                    } else if (producto.isEmpty() || precio.isEmpty()) {
                        Toast.makeText(context, "Completa el producto y el precio", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.CloudUpload, contentDescription = "Subir publicación")
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // --- TARJETA DEL FORMULARIO ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Información básica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = producto,
                        onValueChange = { producto = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Producto") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = precio,
                        onValueChange = { precio = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Precio") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        prefix = { Text("$ ") }
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Descripción") },
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- SECCIÓN DE IMÁGENES ---
            Text(
                "Fotografías",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            OutlinedButton(
                onClick = { picker.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Seleccionar imágenes (${imagenesUris.size})")
            }

            Spacer(Modifier.height(12.dp))

            // Previsualización de imágenes seleccionadas
            if (imagenesUris.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(imagenesUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .width(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            } else {
                // Placeholder visual
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin imágenes seleccionadas", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }

            // Espacio al final para que el FAB no tape contenido
            Spacer(Modifier.height(80.dp))
        }
    }
}

// --- HEADER PERSONALIZADO ---
@Composable
private fun HeaderUploadMarketplace(onBack: () -> Unit) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PostAdd,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Nueva Publicación",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Vende tus artículos en la comunidad",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}