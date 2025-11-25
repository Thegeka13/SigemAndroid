package com.geka.sigem.data.repository

import com.geka.sigem.data.models.CreateAsistenciaDTO
import com.geka.sigem.data.models.Evento
import com.geka.sigem.data.remote.EventoApi
import javax.inject.Inject

class EventoRepository @Inject constructor(
    private val api: EventoApi
) {

    suspend fun getEventos(): List<Evento> {
        return api.getEventos()
    }

    suspend fun getEvento(id: Int): Evento {
        val lista = api.getEvento(id)
        return lista.first()
    }


    suspend fun crearAsistencia(dto: CreateAsistenciaDTO) =
        api.crearAsistencia(dto)

    suspend fun desconfirmar(idAsistencia: Int) =
        api.desconfirmar(idAsistencia)
}
