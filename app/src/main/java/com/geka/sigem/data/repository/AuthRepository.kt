package com.geka.sigem.data.repository

import com.geka.sigem.data.models.LoginRequest
import com.geka.sigem.data.models.UpdateUsuarioRequest
import com.geka.sigem.data.remote.RetrofitClient

class AuthRepository {

    suspend fun login(usuario: String, contrasenia: String) =
        RetrofitClient.authApi.login(LoginRequest(usuario, contrasenia))

    suspend fun updateUsuario(id: Int, request: UpdateUsuarioRequest): Result<Boolean> {
        return try {
            val response = RetrofitClient.authApi.updateUsuario(id, request)

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
