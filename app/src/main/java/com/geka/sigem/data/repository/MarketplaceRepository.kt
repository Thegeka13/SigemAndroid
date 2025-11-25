package com.geka.sigem.data.repository

import com.geka.sigem.data.models.Publicacion
import java.io.File

interface MarketplaceRepository {

    suspend fun getPublicaciones(): List<Publicacion>

    suspend fun getPublicacion(id: Int): Publicacion

    suspend fun getPublicacionesDeUsuario(idUsuario: Int): List<Publicacion>

    suspend fun crearPublicacion(
        producto: String,
        precio: String,
        descripcion: String,
        estatus: Int,
        idUsuario: Int,
        imagenes: List<File>
    ): Publicacion

    suspend fun actualizarPublicacion(
        id: Int,
        producto: String,
        precio: String,
        descripcion: String,
        estatus: Int,
        fotosAEliminar: List<Int>,
        nuevasImagenes: List<File>
    ): Publicacion
}