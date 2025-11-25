package com.geka.sigem.data.repository

import com.geka.sigem.data.models.Publicacion
import com.geka.sigem.data.remote.MarketplaceApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class MarketplaceRepositoryImpl @Inject constructor(
    private val api: MarketplaceApi
) : MarketplaceRepository {

    override suspend fun getPublicaciones(): List<Publicacion> = api.getPublicaciones()

    override suspend fun getPublicacion(id: Int): Publicacion = api.getPublicacion(id)

    override suspend fun getPublicacionesDeUsuario(idUsuario: Int): List<Publicacion> =
        api.getPublicacionesDeUsuario(idUsuario)

    override suspend fun crearPublicacion(
        producto: String,
        precio: String,
        descripcion: String,
        estatus: Int,
        idUsuario: Int,
        imagenes: List<File>
    ): Publicacion {

        // 1. Definimos el tipo de "Texto Plano" para que el servidor lo lea fácil
        val textType = "text/plain".toMediaTypeOrNull()

        // 2. Preparamos las imágenes (estas SÍ llevan su propia lógica)
        val partes = imagenes.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("files", file.name, requestFile)
        }

        return api.createPublicacion(
            // 3. Aplicamos 'textType' a TODOS los campos de texto/números
            producto = producto.toRequestBody(textType),
            precio = precio.toRequestBody(textType),
            descripcion = descripcion.toRequestBody(textType),
            estatus = estatus.toString().toRequestBody(textType),
            idUsuario = idUsuario.toString().toRequestBody(textType), // El que causaba el crash
            files = partes
        )
    }

    override suspend fun actualizarPublicacion(
        id: Int,
        producto: String,
        precio: String,
        descripcion: String,
        estatus: Int,
        fotosAEliminar: List<Int>,
        nuevasImagenes: List<File>
    ): Publicacion {

        // Aplicamos la misma lógica para nuevas imágenes al actualizar
        val filesPart = nuevasImagenes.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("files", file.name, requestFile)
        }

        val fotosAEliminarBody = fotosAEliminar.toString().toRequestBody(MultipartBody.FORM)

        return api.updatePublicacion(
            id = id,
            producto = producto.toRequestBody(MultipartBody.FORM),
            precio = precio.toRequestBody(MultipartBody.FORM),
            descripcion = descripcion.toRequestBody(MultipartBody.FORM),
            estatus = estatus.toString().toRequestBody(MultipartBody.FORM),
            fotosAEliminar = fotosAEliminarBody,
            files = filesPart
        )
    }
}