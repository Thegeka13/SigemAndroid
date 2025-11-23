package com.geka.sigem.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.geka.sigem.data.models.Curso
import com.geka.sigem.ui.viewmodel.CursosViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursoDetalleScreen(
    idCurso: Int,
    idUsuario: Int,
    onBack: () -> Unit,
    viewModel: CursosViewModel = viewModel()
) {
    var curso by remember { mutableStateOf<Curso?>(null) }
    var loading by remember { mutableStateOf(true) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(idCurso) {
        loading = true
        try {
            curso = viewModel.run {
                // cargar directamente desde repo por simplicidad
                kotlinx.coroutines.runBlocking {
                    // NOTA: solo para simplificar la carga inmediata; mejor crear método en VM para getCurso()
                    viewModel.loadCursos() // ya carga lista completa, de donde podemos extraer
                }
                // buscar el curso en la lista cargada
                this.cursos.value.find { it.idCurso == idCurso }
            }
        } catch (e: Exception) {
            mensaje = "Error al cargar curso: ${e.message}"
        } finally {
            loading = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Detalle del curso") }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Atrás") }
        })
    }) { innerPadding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)) {

            if (loading) {
                CircularProgressIndicator()
                return@Surface
            }

            curso?.let { c ->
                Column {
                    Text(c.nombre, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Institución: ${c.institucion}")
                    Text("Inicio: ${c.fechaInicio}")
                    Text("Fin: ${c.fechaFin ?: "-"}")
                    Text("Capacidad: ${c.capacidadMaxima}")
                    Text("Inscritos: ${c.alumnosInscritos}")
                    Text("Disponibles: ${c.lugaresDisponibles}")
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = {
                        viewModel.inscribir(idCurso = c.idCurso, idUsuario = idUsuario) { ok, msg ->
                            mensaje = msg
                        }
                    }) {
                        Text("Inscribirme")
                    }
                    mensaje?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it)
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onBack) { Text("Volver") }
                }
            } ?: run {
                Text(mensaje ?: "Curso no encontrado")
            }
        }
    }
}
