package com.geka.sigem.data.remote

import com.geka.sigem.data.models.Publicacion
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MarketplaceApi {
    @GET("publicacion")
    suspend fun getPublicaciones(): List<Publicacion>

    @GET("publicacion/{id}")
    suspend fun getPublicacion(
        @Path("id") id: Int
    ): Publicacion

    // Endpoint solicitado para "Mis publicaciones"
    @GET("publicacion/usuario/{idUsuario}")
    suspend fun getPublicacionesDeUsuario(
        @Path("idUsuario") idUsuario: Int
    ): List<Publicacion>

    @Multipart
    @POST("publicacion")
    suspend fun createPublicacion(
        @Part("producto") producto: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("estatus") estatus: RequestBody,
        @Part("idUsuario") idUsuario: RequestBody,
        @Part files: List<MultipartBody.Part>? = null
    ): Publicacion

    @Multipart
    @PATCH("publicacion/{id}")
    suspend fun updatePublicacion(
        @Path("id") id: Int,
        @Part("producto") producto: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("estatus") estatus: RequestBody,
        @Part("fotosAEliminar") fotosAEliminar: RequestBody? = null,
        @Part files: List<MultipartBody.Part>? = null
    ): Publicacion
}
