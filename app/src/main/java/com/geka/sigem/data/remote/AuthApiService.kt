package com.geka.sigem.data.remote

import com.geka.sigem.data.models.LoginRequest
import com.geka.sigem.data.models.LoginResponse
import com.geka.sigem.data.models.UpdateUsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @PATCH("usuario/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Int,
        @Body request: UpdateUsuarioRequest
    ): Response<Unit>
}
