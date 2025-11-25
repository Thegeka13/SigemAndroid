package com.geka.sigem.data.repository

import com.geka.sigem.data.models.Apoyo
import com.geka.sigem.data.models.ApoyoInscripcionRequest
import com.geka.sigem.data.models.ApoyoRequest
import com.geka.sigem.data.remote.ApoyoApiService
import com.geka.sigem.data.remote.RetrofitClient
import retrofit2.Response
class ApoyoRepository {
    private val api = RetrofitClient.apoyoApi

    suspend fun getApoyos(): List<Apoyo> = api.getApoyos()

    suspend fun getApoyo(id: Int): Apoyo = api.getApoyo(id)

    suspend fun inscribirEnApoyo(idUsuario: Int, idApoyo: Int): Response<Map<String, Any>> {
        val request = ApoyoInscripcionRequest(idUsuario, idApoyo)
        return api.inscribirEnApoyo(request)
    }

}

