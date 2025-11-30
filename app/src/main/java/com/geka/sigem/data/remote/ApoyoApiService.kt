package com.geka.sigem.data.remote

import com.geka.sigem.data.models.Apoyo
import com.geka.sigem.data.models.ApoyoInscripcionRequest
import com.geka.sigem.data.models.ApoyoRequest
import retrofit2.Response
import retrofit2.http.*

interface ApoyoApiService {

    @GET("apoyo")
    suspend fun getApoyos(): List<Apoyo>

    @GET("apoyo/{idApoyo}")
    suspend fun getApoyo(@Path("idApoyo") id: Int): Apoyo

    @POST("apoyo")
    suspend fun createApoyo(@Body body: ApoyoRequest): Response<Any>

    @PUT("apoyo/{idApoyo}")
    suspend fun updateApoyo(
        @Path("idApoyo") id: Int,
        @Body body: ApoyoRequest
    ): Response<Any>

    @DELETE("apoyo/{idApoyo}")
    suspend fun deleteApoyo(@Path("idApoyo") id: Int): Response<Any>

    @POST("inscripcion-apoyo")
    suspend fun inscribirEnApoyo(
        @Body body: ApoyoInscripcionRequest
    ): Response<Map<String, Any>>

    data class GenericResponse(
        val ok: Boolean,
        val message: String
    )

    @GET("inscripcion-apoyo/usuario/{idUsuario}")
    suspend fun getApoyosInscritos(
        @Path("idUsuario") idUsuario: Int
    ): List<Apoyo>


}
