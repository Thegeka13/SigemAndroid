package com.geka.sigem.data.remote

import com.geka.sigem.data.models.Asistencia
import com.geka.sigem.data.models.CreateAsistenciaDTO
import com.geka.sigem.data.models.Evento
import com.geka.sigem.data.models.SimpleResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface EventoApi {

    @GET("evento/evento")
    suspend fun getEventos(): List<Evento>  // ← CORREGIDO

    @GET("evento/evento/{idEvento}")
    suspend fun getEvento(
        @Path("idEvento") idEvento: Int
    ): List<Evento>

    @POST("evento/asistencia")
    suspend fun crearAsistencia(
        @Body dto: CreateAsistenciaDTO
    ): Asistencia

    @PATCH("evento/asistencia/desconfirmar/{idAsistencia}")
    suspend fun desconfirmar(
        @Path("idAsistencia") idAsistencia: Int
    ): SimpleResponse
}
