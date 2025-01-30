package com.ivandsky.kmpauth.network

import com.ivandsky.kmpauth.local.AuthDataStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

expect fun createHttpClientEngine() : HttpClientEngine

fun createHttpClient(authDataStore: AuthDataStore) : HttpClient =
    HttpClient(createHttpClientEngine()) {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
//        install(Auth) {
//            bearer {
//                sendWithoutRequest { true }
//                loadTokens {
//                    val token = authDataStore.getToken()
//                    token?.let { BearerTokens(accessToken = it, refreshToken = null) }
//                }
//                refreshTokens {
//                    val token = authDataStore.getToken()
//                    token?.let { BearerTokens(accessToken = it, refreshToken = null) }
//                }
//            }
//        }
    }