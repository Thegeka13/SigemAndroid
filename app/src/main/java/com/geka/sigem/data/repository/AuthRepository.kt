package com.geka.sigem.data.repository

import com.geka.sigem.data.models.LoginRequest
import com.geka.sigem.data.models.LoginResponse
import com.geka.sigem.data.remote.RetrofitClient
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository {

    // Función de login con manejo de errores del backend
    suspend fun login(usuario: String, contrasenia: String): LoginResponse {
        return try {
            RetrofitClient.authApi.login(LoginRequest(usuario, contrasenia))
        } catch (e: HttpException) {
            // Capturamos el cuerpo de error del backend
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = errorBody?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (ex: Exception) {
                    "Usuario o contraseña incorrectos"
                }
            } ?: "Usuario o contraseña incorrectos"

            throw Exception(errorMessage)
        } catch (e: Exception) {
            // Otros errores genéricos de red o conversión
            throw Exception(e.message ?: "Error desconocido")
        }
    }
}