package com.ivandsky.kmpauth.data.auth

import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.network.NetworkConstants
import com.ivandsky.kmpauth.util.NetworkState
import com.ivandsky.kmpauth.util.networkRequestFlow
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
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
    fun profile(): Flow<NetworkState<ProfileResponse>> = networkRequestFlow {
        val token = authDataStore.getToken()
        httpClient.get(URL) {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }
    }

    companion object {
        const val URL = "${NetworkConstants.ROOT_URL}/user/"
    }
}