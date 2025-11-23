package com.geka.sigem.data.repository

import com.geka.sigem.data.models.Curso
import com.geka.sigem.data.models.InscritoRequest
import com.geka.sigem.data.remote.RetrofitClient
import retrofit2.Response

class CursoRepository {
    private val api = RetrofitClient.api

    suspend fun getCursos(): List<Curso> = api.getCursos()

    suspend fun getCurso(id: Int): Curso = api.getCurso(id)

    suspend fun inscribir(inscritoRequest: InscritoRequest): Response<Any> =
        api.inscribir(inscritoRequest)
}
