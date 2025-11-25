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

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val sessionManager = SessionManager(application.applicationContext)

    private val _loginState = MutableStateFlow<LoginResponse?>(null)
    val loginState: StateFlow<LoginResponse?> = _loginState

    private val _isLoggedIn = MutableStateFlow<Boolean?>(false)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    // Variable para almacenar idEmpleado
    private var _idEmpleado: Int? = null
    val idEmpleado: Int?
        get() = _idEmpleado

    fun login(usuario: String, contrasenia: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(usuario, contrasenia)

                // Guardar idEmpleado en memoria
                _idEmpleado = response.idEmpleado

                // Guardar idUser de manera persistente
                response.idUser?.let { id ->
                    sessionManager.saveUserId(id)
                }

                _loginState.value = response
                _isLoggedIn.value = true

            } catch (e: Exception) {
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
            sessionManager.clearSession()
            _isLoggedIn.value = false
            _loginState.value = null
            onFinish()
        }
    }
}
