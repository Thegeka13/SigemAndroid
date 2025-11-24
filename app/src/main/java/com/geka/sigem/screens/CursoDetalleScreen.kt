package com.geka.sigem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geka.sigem.ui.viewmodel.CursosViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursoDetalleScreen(
    idCurso: Int,
    idUsuario: Int,
    onBack: () -> Unit,
    viewModel: CursosViewModel = viewModel()
) {
    val curso by viewModel.curso.collectAsState()
    val mensaje by viewModel.message.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(idCurso) {
        viewModel.loadCurso(idCurso)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalles del curso",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(56.dp),
                            strokeWidth = 4.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                curso != null -> {
                    val c = curso!!

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        // Header del curso
                        item {
                            CourseHeader(curso = c)
                        }

                        // Información general
                        item {
                            InformationCard(curso = c)
                        }

                        // Capacidad y disponibilidad
                        item {
                            CapacityCard(curso = c)
                        }

                        // Botón de inscripción
                        item {
                            EnrollmentSection(
                                curso = c,
                                onInscribir = {
                                    viewModel.inscribir(c.idCurso, idUsuario)
                                },
                                mensaje = mensaje
                            )
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            mensaje ?: "Curso no encontrado",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CourseHeader(curso: com.geka.sigem.data.models.Curso) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = curso.nombre,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = curso.institucion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun InformationCard(curso: com.geka.sigem.data.models.Curso) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Información del curso",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            DetailRow(
                icon = Icons.Default.DateRange,
                label = "Fecha de inicio",
                value = curso.fechaInicio
            )

            DetailRow(
                icon = Icons.Default.DateRange,
                label = "Fecha de fin",
                value = curso.fechaFin ?: "No especificada"
            )

            DetailRow(
                icon = Icons.Default.People,
                label = "Capacidad máxima",
                value = "${curso.capacidadMaxima} personas"
            )
        }
    }
}

@Composable
private fun CapacityCard(curso: com.geka.sigem.data.models.Curso) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Disponibilidad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Inscritos
                StatBox(
                    icon = Icons.Default.CheckCircle,
                    label = "Inscritos",
                    value = "${curso.alumnosInscritos}",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )

                // Disponibles
                StatBox(
                    icon = Icons.Default.People,
                    label = "Disponibles",
                    value = "${curso.lugaresDisponibles}",
                    backgroundColor = if (curso.lugaresDisponibles > 0)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer,
                    textColor = if (curso.lugaresDisponibles > 0)
                        MaterialTheme.colorScheme.onSecondaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de progreso
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Ocupación",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${(curso.alumnosInscritos.toFloat() / curso.capacidadMaxima * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { curso.alumnosInscritos.toFloat() / curso.capacidadMaxima },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (curso.lugaresDisponibles > 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatBox(
    icon: ImageVector,
    label: String,
    value: String,
    backgroundColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = textColor
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = textColor,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EnrollmentSection(
    curso: com.geka.sigem.data.models.Curso,
    onInscribir: () -> Unit,
    mensaje: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botón de inscripción
        Button(
            onClick = onInscribir,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = curso.lugaresDisponibles > 0,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (curso.lugaresDisponibles > 0)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                if (curso.lugaresDisponibles > 0) "Inscribirme ahora" else "Sin lugares disponibles",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Mensaje de feedback
        mensaje?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (it.contains("éxito", ignoreCase = true))
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = it,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (it.contains("éxito", ignoreCase = true))
                        MaterialTheme.colorScheme.onSecondaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}