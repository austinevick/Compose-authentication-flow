package com.austinevick.blockrollclone.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "settings")


class DataStore(context: Context) {
    private val dataStore = context.dataStore

    suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): T =
        dataStore.data.first()[key] ?: defaultValue


    suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    suspend fun clearAllPreference() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        val userId = stringPreferencesKey("userId")
        val token = stringPreferencesKey("token")
        val trustToken = stringPreferencesKey("trustToken")
        val username = stringPreferencesKey("username")
        val hasPasscode = booleanPreferencesKey("hasPasscode")
        val email = stringPreferencesKey("email")
        val password = stringPreferencesKey("password")
    }

}