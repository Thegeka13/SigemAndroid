package com.geka.sigem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.InscritoRequest
import com.geka.sigem.data.models.Curso
import com.geka.sigem.data.repository.CursoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

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

    private fun clearMessageDelayed() {
        viewModelScope.launch {
            delay(3000)
            _message.value = null
        }
    }

    fun loadMisCursos(idUsuario: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _misCursos.value = repository.getCursosInscrito(idUsuario)
            } catch (e: Exception) {
                _message.value = "Error al cargar mis cursos"
                clearMessageDelayed()
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadCursos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cursos.value = repository.getCursos()
            } catch (e: Exception) {
                _message.value = "Error al cargar cursos"
                clearMessageDelayed()
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadCurso(idCurso: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _curso.value = repository.getCurso(idCurso)
            } catch (e: Exception) {
                _message.value = "Error al cargar curso"
                clearMessageDelayed()
            } finally {
                _loading.value = false
            }
        }
    }

    fun inscribir(idCurso: Int, idUsuario: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val req = InscritoRequest(idCurso, idUsuario)
                val response = repository.inscribir(req)

                if (response.isSuccessful) {
                    _message.value = "Inscripción exitosa"
                } else {
                    val errorText = response.errorBody()?.string()
                    val parsed = try {
                        JSONObject(errorText ?: "").getString("message")
                    } catch (_: Exception) {
                        "Error desconocido al inscribir"
                    }
                    _message.value = parsed
                }

                clearMessageDelayed()

            } catch (e: Exception) {
                _message.value = "Error al inscribir"
                clearMessageDelayed()
            } finally {
                _loading.value = false
            }
        }
    }
}
