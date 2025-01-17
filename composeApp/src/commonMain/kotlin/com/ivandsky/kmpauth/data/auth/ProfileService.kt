package com.ivandsky.kmpauth.data.auth

import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.util.NetworkState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val username: String,
    val email: String,
    val avatar: String? = null,
    val enabled: Boolean,
)

class ProfileService(
    private val httpClient: HttpClient,
    private val authDataStore: AuthDataStore,
) {
    fun profile(): Flow<NetworkState<ProfileResponse>> = flow {
        emit(NetworkState.Loading)
        val token = authDataStore.getToken()
        val response = httpClient.get(URL) {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }
        emit(NetworkState.Result(response.body()))
    }

    companion object {
        const val URL = "http://192.168.0.120:8000/user/"
    }
}