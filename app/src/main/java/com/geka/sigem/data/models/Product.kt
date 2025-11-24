package com.geka.sigem.data.models

import kotlinx.serialization.Serializable

data class Foto(
    val idFoto: Int,
    val link: String,
    val estatus: String,
    val idPublicacion: Int
)

data class Publicacion(
    val idPublicacion: Int,
    val producto: String,
    val precio: String,         // viene como string ("2000.00")
    val descripcion: String,
    val estatus: String,
    val fotos: List<Foto>
)