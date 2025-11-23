package com.geka.sigem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.Curso
import com.geka.sigem.data.models.InscritoRequest
import com.geka.sigem.data.repository.CursoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CursosViewModel(
    private val repo: CursoRepository = CursoRepository()
) : ViewModel() {

    private val _cursos = MutableStateFlow<List<Curso>>(emptyList())
    val cursos: StateFlow<List<Curso>> = _cursos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun loadCursos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cursos.value = repo.getCursos()
            } catch (e: Exception) {
                _message.value = "Error al obtener cursos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun inscribir(idCurso: Int, idUsuario: Int, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = repo.inscribir(InscritoRequest(idCurso, idUsuario))
                if (resp.isSuccessful) {
                    onResult(true, "Inscripto correctamente")
                    // recargar cursos para actualizar conteo
                    loadCursos()
                } else {
                    val code = resp.code()
                    onResult(false, "Error $code: ${resp.errorBody()?.string() ?: "sin detalle"}")
                }
            } catch (e: Exception) {
                onResult(false, "Excepción: ${e.message}")
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
