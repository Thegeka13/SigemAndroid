package com.geka.sigem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .padding(24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Título
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Usuario
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                // Campo Contraseña
                OutlinedTextField(
                    value = contrasenia,
                    onValueChange = { contrasenia = it },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Contraseña"
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón de login
                Button(
                    onClick = { onLogin(usuario, contrasenia) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Ingresar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}