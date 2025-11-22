package com.geka.sigem

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "sigem_prefs"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class DataStoreManager(private val context: Context) {

    companion object {
        val KEY_LOGGED_IN = booleanPreferencesKey("key_logged_in")
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = false
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val prefs = context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }.first()
        return prefs
    }

    // Expose flow if needed
    fun isLoggedInFlow() = context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }
}
