package com.geka.sigem.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.geka.sigem.ui.viewmodel.AuthViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ChangeCredentialsScreen(
    authViewModel: AuthViewModel,
    onSuccess: () -> Unit
) {

    val context = LocalContext.current

    // PEDIR PERMISO DE NOTIFICACIONES
    SolicitarPermisoNotificaciones()

    val userId = authViewModel.getUsuarioId()

    var usuario by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var repetirContrasenia by remember { mutableStateOf("") }
    var errorContrasenia by remember { mutableStateOf<String?>(null) }

    val updateSuccess by authViewModel.updateSuccess.collectAsState()

    // Cuando se actualizan las credenciales → notificación + navegación
    LaunchedEffect(updateSuccess) {
        if (updateSuccess == true) {
            mostrarNotificacionCredenciales(context)
            authViewModel.resetUpdateState()
            onSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A), // Azul oscuro
                        Color(0xFF3B82F6)  // Azul vibrante
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Icono decorativo
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Modificar Credenciales",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Actualiza tu información de acceso",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card contenedor
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    // Campo Usuario
                    OutlinedTextField(
                        value = usuario,
                        onValueChange = { usuario = it },
                        label = { Text("Nuevo usuario (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            focusedLabelColor = Color(0xFF3B82F6),
                            cursorColor = Color(0xFF3B82F6)
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF3B82F6)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = contrasenia,
                        onValueChange = {
                            contrasenia = it
                            errorContrasenia = null
                        },
                        label = { Text("Nueva contraseña (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            focusedLabelColor = Color(0xFF3B82F6),
                            cursorColor = Color(0xFF3B82F6)
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF3B82F6)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Repetir Contraseña
                    OutlinedTextField(
                        value = repetirContrasenia,
                        onValueChange = {
                            repetirContrasenia = it
                            errorContrasenia = null
                        },
                        label = { Text("Repetir contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            focusedLabelColor = Color(0xFF3B82F6),
                            cursorColor = Color(0xFF3B82F6)
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF3B82F6)
                            )
                        },
                        isError = errorContrasenia != null
                    )

                    if (errorContrasenia != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = errorContrasenia!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Guardar
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        onClick = {

                            val newUser = usuario.takeIf { it.isNotBlank() }
                            val newPass = contrasenia.takeIf { it.isNotBlank() }

                            // Si escribió contraseña nueva, debe coincidir
                            if (!contrasenia.isBlank()) {
                                if (contrasenia != repetirContrasenia) {
                                    errorContrasenia = "Las contraseñas no coinciden"
                                    return@Button
                                }
                            }

                            // Nada para actualizar
                            if (newUser == null && newPass == null) return@Button

                            // Actualizar
                            authViewModel.updateUsuario(
                                id = userId,
                                usuario = newUser,
                                contrasenia = newPass
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6)
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guardar cambios",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mensajes de estado
                    when (updateSuccess) {
                        true -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFDCFCE7)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF16A34A),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Datos actualizados correctamente",
                                        color = Color(0xFF16A34A),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        false -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFEE2E2)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Error al actualizar",
                                        color = Color(0xFFDC2626),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        null -> {}
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SolicitarPermisoNotificaciones() {
    val context = LocalContext.current
    val permission = android.Manifest.permission.POST_NOTIFICATIONS

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        val permisoDado =
            context.checkSelfPermission(permission) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { }
        )

        // Si no está dado → pedirlo
        LaunchedEffect(true) {
            if (!permisoDado) launcher.launch(permission)
        }
    }
}

// ========================================================
// 🔔 NOTIFICACIÓN REAL DEL SISTEMA SIN WARNING
// ========================================================
fun mostrarNotificacionCredenciales(context: Context) {

    val channelId = "credenciales_channel"
    val notificationId = 2024

    // Revisar permiso antes de notificar (evita el warning)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permitido = (
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
                        android.content.pm.PackageManager.PERMISSION_GRANTED
                )
        if (!permitido) return
    }

    // Crear canal en Android 8+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Actualización de Credenciales"
        val description = "Notifica cuando un usuario actualiza sus credenciales"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
        .setContentTitle("Credenciales Actualizadas")
        .setContentText("Tus credenciales fueron actualizadas correctamente.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    NotificationManagerCompat.from(context).notify(notificationId, builder.build())
}