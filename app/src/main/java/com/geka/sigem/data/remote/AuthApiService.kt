package com.geka.sigem.data.remote

import com.geka.sigem.data.models.LoginRequest
import com.geka.sigem.data.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
}
