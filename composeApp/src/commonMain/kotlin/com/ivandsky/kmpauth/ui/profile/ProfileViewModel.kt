package com.ivandsky.kmpauth.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivandsky.kmpauth.data.auth.ProfileService
import com.ivandsky.kmpauth.util.NetworkState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.random.Random

sealed interface ProfileState {
    data class Data(
        val name: String,
        val email: String,
        val avatarUrl: String,
        val isVerified: Boolean,
    ) : ProfileState

    data object Loading : ProfileState
}

class ProfileViewModel(
    private val profileService: ProfileService,
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState = _profileState.asStateFlow()

    init {
        profileService.profile().onEach { state ->
            when(state) {
                is NetworkState.Result -> _profileState.value = ProfileState.Data(
                    state.data.name, state.data.email, state.data.avatarUrl ?: "", state.data.isVerified
                )
                is NetworkState.Error -> TODO()
                NetworkState.Loading -> _profileState.value = ProfileState.Loading
            }
        }
    }

}