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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
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

// Usamos el mismo color de fondo que en tu código de referencia
private val BackgroundGrayy = Color(0xFFF5F5F5)

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

    // Estado de carga para la animación
    var isSaving by remember { mutableStateOf(false) }

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
        containerColor = BackgroundGray,
        // Header personalizado azul, similar al de tu código de referencia
        topBar = {
            HeaderEditMarketplace(onBack = onBack)
        },
        // Botón flotante para Guardar (visible porque es tu publicación)
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isSaving && producto.isNotEmpty() && precio.isNotEmpty()) {
                        isSaving = true
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
                                isSaving = false
                                if (success) {
                                    Toast.makeText(context, "Publicación actualizada", Toast.LENGTH_SHORT).show()
                                    onBack()
                                } else {
                                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = "Guardar cambios")
                }
            }
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

        // Contenido con Scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) // Habilitar scroll para pantallas pequeñas
                .padding(16.dp)
        ) {

            // Tarjeta blanca para los inputs (Estilo limpio)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Detalles del producto",
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
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = precio,
                        onValueChange = { precio = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Precio") },
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

            Text(
                "Gestión de Fotos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )

            // Fotos actuales
            if (pub.fotos.isNotEmpty()) {
                Text(
                    "Actuales:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(pub.fotos) { foto ->
                        if (!fotosAEliminar.contains(foto.idFoto)) {
                            Box(contentAlignment = Alignment.TopEnd) {
                                AsyncImage(
                                    model = foto.link,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .width(120.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                IconButton(
                                    onClick = { fotosAEliminar.add(foto.idFoto) },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color.Red.copy(alpha = 0.7f),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Botón para agregar nuevas (Estilizado)
            OutlinedButton(
                onClick = { picker.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Seleccionar nuevas fotos")
            }

            Spacer(Modifier.height(12.dp))

            // Previsualización de nuevas imágenes
            if (nuevasUris.isNotEmpty()) {
                Text(
                    "Nuevas seleccionadas:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(nuevasUris) { uri ->
                        Box {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(120.dp)
                                    .width(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }

            // Espacio extra al final para que el FAB no tape contenido
            Spacer(Modifier.height(80.dp))
        }
    }
}

// --- HEADER REUTILIZADO Y ADAPTADO ---
@Composable
private fun HeaderEditMarketplace(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            // padding un poco menor que el original para dar espacio al boton back
            .padding(top = 16.dp, bottom = 24.dp, start = 8.dp, end = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de regreso (Importante para navegación)
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit, // Icono de edición
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Editar Publicación",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Actualiza los datos de tu venta",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}