package com.geka.sigem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.Apoyo
import com.geka.sigem.data.remote.ApoyoApiService
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


    fun inscribirEnApoyo(idUsuario: Int, idApoyo: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = repo.inscribirEnApoyo(idUsuario, idApoyo)

                if (response.isSuccessful) {
                    val body = response.body()
                    val ok = body?.get("ok") as? Boolean ?: false
                    val msg = body?.get("message") as? String ?: ""

                    _message.value = if (ok) {
                        "Inscripción realizada correctamente"
                    } else {
                        msg
                    }

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


    fun clearMessage() {
        _message.value = null
    }
}
