package com.geka.sigem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.LoginResponse
import com.geka.sigem.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    // Respuesta completa del login
    private val _loginState = MutableStateFlow<LoginResponse?>(null)
    val loginState: StateFlow<LoginResponse?> = _loginState

    // Estado global de sesión
    private val _isLoggedIn = MutableStateFlow<Boolean?>(false)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    // Guardar el idEmpleado del usuario autenticado
    var idEmpleado: Int? = null
        private set

    fun login(usuario: String, contrasenia: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(usuario, contrasenia)

                // ⬅️ Guarda el idEmpleado aquí
                idEmpleado = response.idEmpleado

                _loginState.value = response
                _isLoggedIn.value = true

            } catch (e: Exception) {
                _loginState.value = null
                _isLoggedIn.value = false
            }
        }
    }

    fun logout(onFinish: () -> Unit) {
        viewModelScope.launch {
            idEmpleado = null
            _isLoggedIn.value = false
            _loginState.value = null
            onFinish()
        }
    }
}

