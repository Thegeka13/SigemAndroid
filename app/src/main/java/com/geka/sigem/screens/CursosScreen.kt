package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geka.sigem.data.models.Curso
import com.geka.sigem.ui.viewmodel.CursosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursosScreen(
    onVerCurso: (Int) -> Unit,
    viewModel: CursosViewModel = viewModel()
) {
    val cursos by viewModel.cursos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCursos()
    }

    Scaffold { innerPadding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
        ) {
            Column {
                Text("Cursos disponibles", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))

                if (loading) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn {
                        items(cursos) { curso ->
                            CursoRow(curso = curso, onVerCurso = onVerCurso)
                        }
                    }
                }

                message?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun CursoRow(curso: Curso, onVerCurso: (Int) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = curso.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "Institución: ${curso.institucion}")
            Text(text = "Inicio: ${curso.fechaInicio}  Fin: ${curso.fechaFin ?: "-"}")
            Text(text = "Capacidad: ${curso.capacidadMaxima}  Inscritos: ${curso.alumnosInscritos}")
            Text(text = "Lugares disponibles: ${curso.lugaresDisponibles}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { onVerCurso(curso.idCurso) }) {
                    Text("Ver curso")
                }
            }
        }
    }
}
