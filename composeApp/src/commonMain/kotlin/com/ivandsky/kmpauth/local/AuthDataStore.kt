package com.ivandsky.kmpauth.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthDataStore(
    private val dataStore: DataStore<Preferences>
) {
    fun getTokenFlow(): Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }.distinctUntilChanged()
    suspend fun getToken() = getTokenFlow().first()

    fun getEmailFlow(): Flow<String?> = dataStore.data.map { it[EMAIL_KEY] }.distinctUntilChanged()
    suspend fun getEmail() = getEmailFlow().first()

    suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { it[EMAIL_KEY] = email }
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }

    suspend fun clearEmail() {
        dataStore.edit { it.remove(EMAIL_KEY) }
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("TOKEN")
        val EMAIL_KEY = stringPreferencesKey("EMAIL")
    }
}