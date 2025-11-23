package com.geka.sigem.data.repository

import com.geka.sigem.data.models.LoginRequest
import com.geka.sigem.data.remote.RetrofitClient

class AuthRepository {

    suspend fun login(usuario: String, contrasenia: String) =
        RetrofitClient.authApi.login(LoginRequest(usuario, contrasenia))
}
