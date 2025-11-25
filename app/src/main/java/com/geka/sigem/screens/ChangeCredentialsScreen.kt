package com.geka.sigem.screens

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.geka.sigem.ui.viewmodel.AuthViewModel

@Composable
fun ChangeCredentialsScreen(
    authViewModel: AuthViewModel,
    onSuccess: () -> Unit
) {

    var usuario by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }

    val updateSuccess by authViewModel.updateSuccess.collectAsState()

    LaunchedEffect(updateSuccess) {
        if (updateSuccess == true) {
            authViewModel.resetUpdateState()
            onSuccess()
        }
    }

    Column(Modifier.padding(16.dp)) {

        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Nuevo usuario (opcional)") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = contrasenia,
            onValueChange = { contrasenia = it },
            label = { Text("Nueva contraseña (opcional)") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newUser = usuario.takeIf { it.isNotBlank() }
                val newPass = contrasenia.takeIf { it.isNotBlank() }

                // Validación: no permitir todo vacío
                if (newUser == null && newPass == null) return@Button

                authViewModel.updateUsuario(
                    id = authViewModel.idUsuario ?: -1,
                    usuario = newUser,
                    contrasenia = newPass
                )
            }
        ) {
            Text("Guardar cambios")
        }

        when (updateSuccess) {
            true -> Text("Datos actualizados correctamente")
            false -> Text("Error al actualizar")
            null -> {}
        }
    }
}
