package com.geka.sigem.data.remote

import com.geka.sigem.data.models.CrearSolicitudRequest
import com.geka.sigem.data.models.SolicitudVacaciones
import retrofit2.http.*

interface SolicitudApiService {

    @POST("solicitud-vacaciones/solicitar")
    suspend fun crearSolicitud(
        @Body request: CrearSolicitudRequest
    ): SolicitudVacaciones

    @GET("solicitud-vacaciones/{idEmpleado}/solicitudes")
    suspend fun obtenerSolicitudesEmpleado(
        @Path("idEmpleado") idEmpleado: Int
    ): List<SolicitudVacaciones>
}
