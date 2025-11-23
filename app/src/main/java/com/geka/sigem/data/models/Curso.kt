package com.geka.sigem.data.models

data class TipoCurso(
    val idTipoCurso: Int?,
    val nombre: String?
)

data class Curso(
    val idCurso: Int,
    val nombre: String,
    val institucion: String,
    val fechaInicio: String,
    val fechaFin: String?,
    val capacidadMaxima: Int,
    val alumnosInscritos: Int,
    val enlace: String?,
    val tipoCurso: TipoCurso?
) {
    val lugaresDisponibles: Int
        get() = capacidadMaxima - alumnosInscritos
}
