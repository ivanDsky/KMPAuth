package com.ivandsky.kmpauth.usecase

import com.ivandsky.kmpauth.local.AuthDataStore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

class LogoutUseCase(
    private val authDataStore: AuthDataStore,
    private val httpClient: HttpClient,
) {
    suspend operator fun invoke() {
        authDataStore.clearToken()
        httpClient.authProvider<BearerAuthProvider>()?.clearToken()
    }
}