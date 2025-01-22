package com.ivandsky.kmpauth.data.auth

import com.ivandsky.kmpauth.network.NetworkConstants
import com.ivandsky.kmpauth.util.NetworkState
import com.ivandsky.kmpauth.util.networkRequestFlow
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
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)

@Serializable
data class ValidationRequest(
    val email: String,
    val verificationCode: String,
)

@Serializable
data class LoginResponse(
    val token: String
)

class AuthService(
    private val httpClient: HttpClient,
) {
    fun login(request: LoginRequest): Flow<NetworkState<LoginResponse>> = networkRequestFlow {
        httpClient.post("$URL/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    fun register(request: RegisterRequest): Flow<NetworkState<Unit>> = networkRequestFlow {
        httpClient.post("$URL/sign-up") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    fun validate(request: ValidationRequest): Flow<NetworkState<Unit>> = networkRequestFlow {
        httpClient.post("$URL/verify") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    fun resend(email: String) : Flow<NetworkState<Unit>> = networkRequestFlow {
        httpClient.post("$URL/resend") {
            setBody(email)
        }
    }

    companion object {
        const val URL = "${NetworkConstants.ROOT_URL}/auth"
    }
}