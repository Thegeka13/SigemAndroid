package com.geka.sigem.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geka.sigem.ui.viewmodel.SolicitudViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearSolicitudScreen(
    idEmpleado: Int,
    viewModel: SolicitudViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit
) {
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }

    var showDatePickerInicio by remember { mutableStateOf(false) }
    var showDatePickerFin by remember { mutableStateOf(false) }

    val datePickerStateInicio = rememberDatePickerState()
    val datePickerStateFin = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Barra superior
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF1A1A1A)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Nueva Solicitud",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Solicitud de vacaciones",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }

        // Contenido del formulario
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Información del período",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A1A1A)
                    )

                    // Campo Fecha Inicio
                    OutlinedTextField(
                        value = fechaInicio,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Fecha de inicio") },
                        placeholder = { Text("Seleccionar fecha") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Calendario"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2563EB),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            IconButton(onClick = { showDatePickerInicio = true }) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Seleccionar fecha",
                                    tint = Color(0xFF2563EB)
                                )
                            }
                        }
                    )

                    // Campo Fecha Fin
                    OutlinedTextField(
                        value = fechaFin,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Fecha de fin") },
                        placeholder = { Text("Seleccionar fecha") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Calendario"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2563EB),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            IconButton(onClick = { showDatePickerFin = true }) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Seleccionar fecha",
                                    tint = Color(0xFF2563EB)
                                )
                            }
                        }
                    )

                    Divider(
                        color = Color(0xFFE0E0E0),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Text(
                        text = "Detalles adicionales",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A1A1A)
                    )

                    // Campo Motivo
                    OutlinedTextField(
                        value = motivo,
                        onValueChange = { motivo = it },
                        label = { Text("Motivo de la solicitud") },
                        placeholder = { Text("Describe el motivo de tu solicitud") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2563EB),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 4
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6B7280)
                    )
                ) {
                    Text(
                        text = "Cancelar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = {
                        viewModel.crearSolicitud(
                            idEmpleado,
                            fechaInicio,
                            fechaFin,
                            motivo
                        ) {
                            onBack()
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = fechaInicio.isNotEmpty() && fechaFin.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Enviar Solicitud",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // DatePicker para fecha de inicio
    if (showDatePickerInicio) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerInicio = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerStateInicio.selectedDateMillis?.let { millis ->
                        val date = LocalDate.ofEpochDay(millis / 86400000)
                        fechaInicio = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    }
                    showDatePickerInicio = false
                }) {
                    Text("Aceptar", color = Color(0xFF2563EB))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerInicio = false }) {
                    Text("Cancelar", color = Color(0xFF6B7280))
                }
            }
        ) {
            DatePicker(
                state = datePickerStateInicio,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color(0xFF2563EB)
                )
            )
        }
    }

    // DatePicker para fecha de fin
    if (showDatePickerFin) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerFin = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerStateFin.selectedDateMillis?.let { millis ->
                        val date = LocalDate.ofEpochDay(millis / 86400000)
                        fechaFin = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    }
                    showDatePickerFin = false
                }) {
                    Text("Aceptar", color = Color(0xFF2563EB))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerFin = false }) {
                    Text("Cancelar", color = Color(0xFF6B7280))
                }
            }
        ) {
            DatePicker(
                state = datePickerStateFin,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color(0xFF2563EB)
                )
            )
        }
    }
}