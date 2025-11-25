package com.geka.sigem.data.repository

import com.geka.sigem.data.models.CrearSolicitudRequest
import com.geka.sigem.data.remote.RetrofitClient

class SolicitudRepository {

    private val api = RetrofitClient.solicitudApi

    suspend fun crearSolicitud(request: CrearSolicitudRequest) =
        api.crearSolicitud(request)

    suspend fun obtenerSolicitudesEmpleado(idEmpleado: Int) =
        api.obtenerSolicitudesEmpleado(idEmpleado)
}