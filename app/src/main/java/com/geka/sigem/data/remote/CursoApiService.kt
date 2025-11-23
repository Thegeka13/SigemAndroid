package com.geka.sigem.data.remote

import com.geka.sigem.data.models.Curso
import com.geka.sigem.data.models.InscritoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CursoApiService {
    @GET("curso")
    suspend fun getCursos(): List<Curso>

    @GET("curso/{idCurso}")
    suspend fun getCurso(@Path("idCurso") id: Int): Curso

    @POST("inscrito")
    suspend fun inscribir(@Body body: InscritoRequest): Response<Any>
}
