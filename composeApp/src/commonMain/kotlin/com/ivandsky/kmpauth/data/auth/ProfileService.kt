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
    val id: Long,
    val username: String,
    val email: String,
    val avatar: String? = null,
    val enabled: Boolean,
    val roles: List<String>,
)

class ProfileService(
    private val httpClient: HttpClient,
    private val authDataStore: AuthDataStore,
) {
    fun profile(id: Long = -1L): Flow<NetworkState<ProfileResponse>> = networkRequestFlow {
        val token = authDataStore.getToken()
        val url = if(id == -1L) "$URL/" else "$URL/${id}"
        httpClient.get(url) {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }
    }

    fun allProfiles(): Flow<NetworkState<List<ProfileResponse>>> = networkRequestFlow {
        val token = authDataStore.getToken()
        httpClient.get("$URL/all") {
            headers { append(HttpHeaders.Authorization, "Bearer $token") }
        }
    }

    companion object {
        const val URL = "${NetworkConstants.ROOT_URL}/user"
    }
}