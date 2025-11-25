package com.geka.sigem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.CrearSolicitudRequest
import com.geka.sigem.data.models.SolicitudVacaciones
import com.geka.sigem.data.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SolicitudViewModel : ViewModel() {

    private val repository = SolicitudRepository()

    private val _solicitudes = MutableStateFlow<List<SolicitudVacaciones>>(emptyList())
    val solicitudes: StateFlow<List<SolicitudVacaciones>> = _solicitudes

    fun cargarSolicitudes(idEmpleado: Int) {
        viewModelScope.launch {
            _solicitudes.value = repository.obtenerSolicitudesEmpleado(idEmpleado)
        }
    }

    fun crearSolicitud(idEmpleado: Int, inicio: String, fin: String, motivo: String, onFinish: () -> Unit) {
        viewModelScope.launch {
            repository.crearSolicitud(
                CrearSolicitudRequest(
                    idEmpleado,
                    inicio,
                    fin,
                    motivo
                )
            )
            onFinish()
        }
    }
}
