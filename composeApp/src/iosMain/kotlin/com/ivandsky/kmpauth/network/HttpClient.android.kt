package com.ivandsky.kmpauth.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun createHttpClientEngine(): HttpClientEngine = Darwin.create()