package com.geka.sigem.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.LoginResponse
import com.geka.sigem.data.repository.AuthRepository
import com.geka.sigem.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Heredamos de AndroidViewModel para tener acceso a 'application' (Contexto)
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // Instancias manuales (Modo Híbrido / Sin Hilt para esta clase)
    private val repository = AuthRepository()
    private val sessionManager = SessionManager(application.applicationContext)

    // ESTADOS (UI) -----------------------------------------------------------

    // Respuesta completa del login
    private val _loginState = MutableStateFlow<LoginResponse?>(null)
    val loginState: StateFlow<LoginResponse?> = _loginState

    // Estado global de sesión
    private val _isLoggedIn = MutableStateFlow<Boolean?>(false)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    // FUNCIONES --------------------------------------------------------------

    fun login(usuario: String, contrasenia: String) {
        viewModelScope.launch {
            try {
                // 1. Llamada a la API
                val response = repository.login(usuario, contrasenia)

                // Guarda el idEmpleado aquí
                idEmpleado = response.idEmpleado
                // 2. GUARDADO PERSISTENTE:
                // Si la respuesta trae ID, lo guardamos en el celular (SharedPreferences)
                // para que el MarketplaceViewModel (con Hilt) pueda leerlo después.
                response.idUser?.let { id ->
                    sessionManager.saveUserId(id)
                }

                // 3. Actualizamos la UI
                _loginState.value = response
                _isLoggedIn.value = true

            } catch (e: Exception) {
                // Manejo de error
                _loginState.value = null
                _isLoggedIn.value = false
            }
        }
    }
    fun getUsuarioId(): Int {
        return sessionManager.getUserId()
    }

    fun logout(onFinish: () -> Unit) {
        viewModelScope.launch {
            // 1. Borramos los datos del disco (SharedPreferences)
            sessionManager.clearSession()

            // 2. Limpiamos el estado de la UI
            _isLoggedIn.value = false
            _loginState.value = null

            // 3. Ejecutamos la acción de navegación (ir al login)
            onFinish()
        }
    }
}
