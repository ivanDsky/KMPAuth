package com.ivandsky.kmpauth.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

expect fun createHttpClientEngine() : HttpClientEngine

fun createHttpClient() : HttpClient =
    HttpClient(createHttpClientEngine()) {
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

val httpClientModule = module {
    single { createHttpClient() }
}