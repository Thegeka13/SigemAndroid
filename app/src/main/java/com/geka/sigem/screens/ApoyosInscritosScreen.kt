package com.geka.sigem.ui.inscritos

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geka.sigem.data.models.Apoyo
import com.geka.sigem.ui.viewmodel.ApoyosViewModel

// Colores consistentes con ApoyoDetalleScreen
val AzulPrimario1 = Color(0xFF0D5C96)
val AzulClaro1 = Color(0xFF1976D2)
val GrisClaro1 = Color(0xFFF5F5F5)
val GrisTexto1 = Color(0xFF666666)
val VerdeCheck1 = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApoyosInscritosScreen(
    idUsuario: Int,
    onBack: () -> Unit = {},
    viewModel: ApoyosViewModel = viewModel()
) {
    val apoyos by viewModel.apoyosInscritos.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(idUsuario) {
        viewModel.loadApoyosInscritos(idUsuario)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis Apoyos Inscritos",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulPrimario1
                )
            )
        },
        containerColor = GrisClaro1
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            when {
                loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            strokeWidth = 4.dp,
                            color = AzulPrimario1
                        )
                        Text(
                            "Cargando tus apoyos...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrisTexto1
                        )
                    }
                }

                apoyos.isEmpty() -> {
                    EmptyStateContent()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ApoyosHeader(cantidad = apoyos.size)
                        }

                        items(apoyos) { apoyo ->
                            ApoyoInscritoCard(apoyo)
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApoyosHeader(cantidad: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Apoyos Activos",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto1,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = cantidad.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario1,
                    fontSize = 32.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AzulClaro1.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = VerdeCheck1,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyStateContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = GrisTexto1
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sin apoyos inscritos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AzulPrimario1
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Aún no estás inscrito en ningún apoyo.\n¡Explora y regístrate!",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto1,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Navegar a explorar apoyos */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulPrimario1
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Explorar Apoyos", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ApoyoInscritoCard(apoyo: Apoyo) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(AzulClaro1.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = AzulPrimario1,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = apoyo.nombre ?: "Sin nombre",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AzulPrimario1,
                            fontSize = 16.sp
                        )
                    }
                }

                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = VerdeCheck1
                        )
                        Text(
                            text = "Activo",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = GrisClaro1,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = AzulClaro1,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = apoyo.descripcion ?: "Sin descripción",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    lineHeight = 20.sp,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
            }


        }
    }
}