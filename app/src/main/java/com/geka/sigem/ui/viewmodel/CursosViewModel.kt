package com.geka.sigem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.InscritoRequest
import com.geka.sigem.data.models.Curso
import com.geka.sigem.data.repository.CursoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CursosViewModel : ViewModel() {

    private val repository = CursoRepository()

    private val _cursos = MutableStateFlow<List<Curso>>(emptyList())
    val cursos: StateFlow<List<Curso>> = _cursos

    private val _curso = MutableStateFlow<Curso?>(null)
    val curso: StateFlow<Curso?> = _curso

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message


    private val _misCursos = MutableStateFlow<List<Curso>>(emptyList())
    val misCursos: StateFlow<List<Curso>> = _misCursos

    fun setMessage(msg: String?) {
        _message.value = msg
    }


    fun loadMisCursos(idUsuario: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _misCursos.value = repository.getCursosInscrito(idUsuario)
            } catch (e: Exception) {
                _message.value = "Error al cargar mis cursos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    // ------------------------------
    // CARGAR LISTA DE CURSOS
    // ------------------------------
    fun loadCursos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cursos.value = repository.getCursos()
            } catch (e: Exception) {
                _message.value = "Error al cargar cursos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }


    // ------------------------------
    // CARGAR CURSO INDIVIDUAL
    // ------------------------------
    fun loadCurso(idCurso: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _curso.value = repository.getCurso(idCurso)
            } catch (e: Exception) {
                _message.value = "Error al cargar curso: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }


    // ------------------------------
    // INSCRIBIR USUARIO A CURSO
    // ------------------------------
    fun inscribir(idCurso: Int, idUsuario: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val req = InscritoRequest(idCurso, idUsuario)
                val response = repository.inscribir(req)

                if (response.isSuccessful) {
                    _message.value = "Inscripción exitosa"
                } else {
                    _message.value = "Error al inscribir: ${response.errorBody()?.string()}"
                }

            } catch (e: Exception) {
                _message.value = "Error al inscribir: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
