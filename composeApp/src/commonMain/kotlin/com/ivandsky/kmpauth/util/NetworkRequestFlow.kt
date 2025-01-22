package com.ivandsky.kmpauth.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

inline fun<reified T> networkRequestFlow(crossinline apiCall: suspend () -> HttpResponse) = flow<NetworkState<T>> {
    emit(NetworkState.Loading)
    val response = apiCall()
    when(response.status) {
        HttpStatusCode.OK -> emit(NetworkState.Result(response.body<T>()))
        HttpStatusCode.InternalServerError -> emit(NetworkState.Error("Sorry, server is down"))
        else -> emit(NetworkState.Error(response.bodyAsText()))
    }
}.catch {
    emit(NetworkState.Error(it.message ?: "Unexpected behaviour"))
}