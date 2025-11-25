package com.geka.sigem.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geka.sigem.data.models.LoginResponse
import com.geka.sigem.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import com.geka.sigem.data.models.UpdateUsuarioRequest


class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<LoginResponse?>(null)
    val loginState: StateFlow<LoginResponse?> = _loginState

    private val _isLoggedIn = MutableStateFlow<Boolean?>(false)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    var idEmpleado: Int? = null
        private set

    var idUsuario: Int? = null
        private set



    // ----------------------
    // LOGIN
    // ----------------------
    fun login(usuario: String, contrasenia: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(usuario, contrasenia)
                idUsuario = response.idUser
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


    // ----------------------
    // UPDATE CREDENTIALS
    // ----------------------

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    fun resetUpdateState() {
        _updateSuccess.value = null   // ← IMPORTANTE
    }

    fun updateUsuario(id: Int, usuario: String?, contrasenia: String?) {
        viewModelScope.launch {
            try {
                val request = UpdateUsuarioRequest(usuario, contrasenia)
                val result = repository.updateUsuario(id, request)

                _updateSuccess.value = result.isSuccess

            } catch (e: Exception) {
                _updateSuccess.value = false
            }
        }
    }
}


