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

    // Estado del login
    private val _loginState = MutableStateFlow<LoginResponse?>(null)
    private val _loginAttempted = MutableStateFlow(false)
    val loginState: StateFlow<LoginResponse?> = _loginState

    //Update usuario
    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError


    // Estado de sesión
    private val _isLoggedIn = MutableStateFlow<Boolean?>(false)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    // ------------------------------ LOGIN ------------------------------


    // ------------------------------ SPLASH -----------------------------
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess


    fun login(usuario: String, contrasenia: String) {
        viewModelScope.launch {
            _loginError.value = null
            try {
                val response = repository.login(usuario, contrasenia)
                response.idUser?.let { sessionManager.saveUserId(it) }
                response.idEmpleado?.let { sessionManager.saveEmpleadoId(it) }

                _loginState.value = response
                _isLoggedIn.value = true
                _loginSuccess.value = true  // <--- clave para navegar

            } catch (e: Exception) {
                _loginState.value = null
                _isLoggedIn.value = false
                _loginError.value = e.message
                _loginSuccess.value = false
            }
        }
    }




    // --------------------------- OBTENER ID ----------------------------

    fun getUsuarioId(): Int {
        return sessionManager.getUserId()
    }

    fun getEmpleadoId(): Int {
        return sessionManager.getEmpleadoId()
    }


    // ------------------------------ LOGOUT ------------------------------

    fun logout(onFinish: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()

            _isLoggedIn.value = false
            _loginState.value = null

            onFinish()
        }
    }

    // Update
    fun updateUsuario(id: Int, usuario: String?, contrasenia: String?) {
        viewModelScope.launch {
            try {
                val result = repository.updateUsuario(id, usuario, contrasenia)
                _updateSuccess.value = result   // true o false según el backend
            } catch (e: Exception) {
                _updateSuccess.value = false
            }
        }
    }

    fun resetUpdateState() {
        _updateSuccess.value = null
    }
}