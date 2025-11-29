package com.geka.sigem.data.models

import com.google.gson.annotations.SerializedName

data class SolicitudVacaciones(
    val idSolicitud: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val estado: String,
    val comentarioAdministrador: String?
)

