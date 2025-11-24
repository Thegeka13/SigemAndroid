package com.geka.sigem.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_ID = "user_id"
    }

    // Guardar ID (Lo usaremos en el Login)
    fun saveUserId(id: Int) {
        prefs.edit().putInt(KEY_USER_ID, id).apply()
    }

    // Leer ID (Lo usaremos al crear la publicación)
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1) // Retorna -1 si no hay usuario
    }

    // Cerrar sesión
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}