package com.geka.sigem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application.applicationContext)

    // null = loading, true = logged in, false = not logged in
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    init {
        viewModelScope.launch {
            val logged = dataStoreManager.isLoggedIn()
            _isLoggedIn.value = logged
        }
    }

    fun login(password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (password == "1234") {
                dataStoreManager.setLoggedIn(true)
                _isLoggedIn.value = true
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun logout(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            dataStoreManager.clearSession()
            _isLoggedIn.value = false
            onComplete?.invoke()
        }
    }
}
