package com.geka.sigem.data.models

data class Evento(
    val idEvento: Int,
    val nombre: String,          // ← antes era titulo
    val descripcion: String,
    val fechaInicio: String,     // ← antes era fecha
    val tipo: String,
    val bannerLink: String?,     // ← NUEVO: imagen del evento
    val confirmaciones : List<Confirmacion>,
    val asistencias: List<Asistencia>
)



data class Asistencia(
    val id: Int,
    val idEvento: Int,
    val idUsuario: Int
)


data class Confirmacion(
    val idConfirmacion: Int,
    val confirmado: String,
    val enterado: String
)

data class CreateAsistenciaDTO(
    val idEvento: Int,
    val idUsuario: Int
)