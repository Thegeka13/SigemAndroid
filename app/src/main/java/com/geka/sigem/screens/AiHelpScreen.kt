package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class ChatMessage(
    val isUser: Boolean,
    val text: String
)

@Composable
fun AiHelpScreen() {
    val scope = rememberCoroutineScope()

    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var currentMessage by remember { mutableStateOf("") }

    // En el modelo Cloud, el "loading" es solo durante la petición de red
    var isLoadingResponse by remember { mutableStateOf(false) }

    // ➤ CONFIGURACIÓN DEL MODELO GEMINI (CLOUD)
    // Se usa 'gemini-1.5-flash' porque es rápido y económico para chat
    val model = remember {
        GenerativeModel(
            modelName = "gemini-2.0-flash-lite",
            apiKey = "AIzaSyDo9oZp-_Po9nktsqujiDG9jngvvLVKx7g" // ⚠️ REEMPLAZA ESTO CON TU API KEY
        )
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
                .padding(16.dp)
        ) {

            // Header con icono
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF3B82F6)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Asistente Sigem AI",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de mensajes con fondo blanco
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    reverseLayout = true
                ) {
                    items(messages.reversed()) { msg ->
                        MessageBubble(msg)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Área de input
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentMessage,
                        onValueChange = { currentMessage = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escribe aquí...") },
                        enabled = !isLoadingResponse,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            focusedLabelColor = Color(0xFF3B82F6),
                            cursorColor = Color(0xFF3B82F6)
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (currentMessage.isNotBlank()) {
                                val userText = currentMessage
                                // Añade el mensaje de usuario
                                messages = messages + ChatMessage(true, userText)
                                currentMessage = ""
                                isLoadingResponse = true

                                // Añade placeholder "Escribiendo..."
                                messages = messages + ChatMessage(false, "...")

                                scope.launch {
                                    try {
                                        // ➤ LLAMADA AL MODELO (Generar respuesta)
                                        val response = model.generateContent(userText)

                                        // Elimina el placeholder y pone la respuesta real
                                        messages = messages.dropLast(1) + ChatMessage(
                                            false,
                                            response.text ?: "No pude generar una respuesta de texto."
                                        )
                                    } catch (e: Exception) {
                                        messages = messages.dropLast(1) + ChatMessage(
                                            false,
                                            "Error: ${e.localizedMessage}"
                                        )
                                    } finally {
                                        isLoadingResponse = false
                                    }
                                }
                            }
                        },
                        enabled = currentMessage.isNotBlank() && !isLoadingResponse,
                        modifier = Modifier.size(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        if (isLoadingResponse) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Enviar",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(msg: ChatMessage) {
    val bubbleColor = if (msg.isUser) {
        Color(0xFF3B82F6) // Azul para usuario
    } else {
        Color(0xFFF1F5F9) // Gris claro para AI
    }

    val textColor = if (msg.isUser) {
        Color.White
    } else {
        Color(0xFF1E293B)
    }

    val alignment = if (msg.isUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (msg.isUser) 16.dp else 4.dp,
                bottomEnd = if (msg.isUser) 4.dp else 16.dp
            ),
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = msg.text,
                modifier = Modifier.padding(12.dp),
                color = textColor,
                fontSize = 15.sp
            )
        }
    }
}