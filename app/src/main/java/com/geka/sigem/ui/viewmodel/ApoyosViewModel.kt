package com.geka.sigem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.Apoyo
import com.geka.sigem.data.repository.ApoyoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApoyosViewModel(
    private val repo: ApoyoRepository = ApoyoRepository()
) : ViewModel() {

    private val _apoyos = MutableStateFlow<List<Apoyo>>(emptyList())
    val apoyos: StateFlow<List<Apoyo>> = _apoyos

    private val _apoyo = MutableStateFlow<Apoyo?>(null)
    val apoyo: StateFlow<Apoyo?> = _apoyo

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // -------------------------------
    // NUEVO: Lista de apoyos inscritos
    // -------------------------------
    private val _apoyosInscritos = MutableStateFlow<List<Apoyo>>(emptyList())
    val apoyosInscritos: StateFlow<List<Apoyo>> = _apoyosInscritos


    // -------------------------
    // Obtener todos los apoyos
    // -------------------------
    fun loadApoyos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _apoyos.value = repo.getApoyos()
            } catch (e: Exception) {
                _message.value = "Error al obtener apoyos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }


    // -------------------------
    // Obtener un apoyo específico
    // -------------------------
    fun loadApoyo(idApoyo: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _apoyo.value = repo.getApoyo(idApoyo)
            } catch (e: Exception) {
                _message.value = "Error al obtener el apoyo: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }


    // -------------------------
    // Inscribir usuario a un apoyo
    // -------------------------
    fun inscribirEnApoyo(idUsuario: Int, idApoyo: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repo.inscribirEnApoyo(idUsuario, idApoyo)

                if (response.isSuccessful) {
                    val body = response.body()
                    val msg = body?.get("message") as? String ?: "Inscripción exitosa"

                    _message.value = msg

                } else {
                    _message.value = "Error en el servidor"
                }

            } catch (e: Exception) {
                _message.value = "Error de red: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }


    // -------------------------------------------------------
    // NUEVO: Obtener apoyos en los que el usuario está inscrito
    // -------------------------------------------------------
    fun loadApoyosInscritos(idUsuario: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _apoyosInscritos.value = repo.getApoyosInscritos(idUsuario)
            } catch (e: Exception) {
                _message.value = "Error al obtener apoyos inscritos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }



    fun clearMessage() {
        _message.value = null
    }
}
