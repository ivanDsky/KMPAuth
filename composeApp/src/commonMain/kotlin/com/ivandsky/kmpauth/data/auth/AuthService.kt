package com.ivandsky.kmpauth.data.auth

import com.ivandsky.kmpauth.util.NetworkState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class LoginResponse(
    val token: String
)

class AuthService(
    private val httpClient: HttpClient,
) {
    fun login(request: LoginRequest): Flow<NetworkState<LoginResponse>> = flow {
        emit(NetworkState.Loading)
        val response = httpClient.post("$URL/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        emit(NetworkState.Result(response.body()))
    }

    companion object {
        const val URL = "http://192.168.0.120:8000/auth"
    }
}