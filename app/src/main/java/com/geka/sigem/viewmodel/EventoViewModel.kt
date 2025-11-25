package com.geka.sigem.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.CreateAsistenciaDTO
import com.geka.sigem.data.models.Evento
import com.geka.sigem.data.repository.EventoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventoViewModel @Inject constructor(
    private val repo: EventoRepository
) : ViewModel() {

    var eventos by mutableStateOf<List<Evento>?>(null)
        private set

    var evento by mutableStateOf<Evento?>(null)
        private set

    var loading by mutableStateOf(false)
        private set

    fun cargarEventos() = viewModelScope.launch {
        loading = true
        eventos = repo.getEventos()
        loading = false
    }

    fun cargarEvento(id: Int) = viewModelScope.launch {
        loading = true
        evento = repo.getEvento(id)
        loading = false
    }

    fun confirmarAsistencia(idEvento: Int, idUsuario: Int) = viewModelScope.launch {
        repo.crearAsistencia(CreateAsistenciaDTO(idEvento, idUsuario))
        cargarEvento(idEvento)
    }

    fun desconfirmar(idAsistencia: Int, idEvento: Int) = viewModelScope.launch {
        repo.desconfirmar(idAsistencia)
        cargarEvento(idEvento)
    }
}
