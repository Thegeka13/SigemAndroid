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
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_EMPLEADO_ID = "empleado_id"
    }

    fun saveUserId(id: Int) {
        prefs.edit().putInt(KEY_USER_ID, id).apply()
    }

    fun saveEmpleadoId(id: Int) {
        prefs.edit().putInt(KEY_EMPLEADO_ID, id).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun getEmpleadoId(): Int {
        return prefs.getInt(KEY_EMPLEADO_ID, -1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}