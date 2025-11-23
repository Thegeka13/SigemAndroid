package com.geka.sigem.data.models

data class CrearSolicitudRequest(
    val idEmpleado: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val motivo: String
)
