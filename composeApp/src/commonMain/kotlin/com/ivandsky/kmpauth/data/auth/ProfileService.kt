package com.ivandsky.kmpauth.data.auth

import com.ivandsky.kmpauth.util.NetworkState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val isVerified: Boolean,
)

class ProfileService(
    private val httpClient: HttpClient,
) {
    fun profile(): Flow<NetworkState<ProfileResponse>> = flow {
        emit(NetworkState.Loading)
        val response = httpClient.get("$URL/user/")
        emit(NetworkState.Result(response.body()))
    }

    companion object {
        const val URL = "localhost:8000"
    }
}