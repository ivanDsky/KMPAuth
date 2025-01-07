package com.ivandsky.kmpauth.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.JsClient

actual fun createHttpClientEngine(): HttpClientEngine = JsClient().create()