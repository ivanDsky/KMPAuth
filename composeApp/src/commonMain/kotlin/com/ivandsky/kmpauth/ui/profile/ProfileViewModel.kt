package com.ivandsky.kmpauth.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivandsky.kmpauth.data.auth.ProfileService
import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.usecase.LogoutUseCase
import com.ivandsky.kmpauth.util.NetworkState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
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
    private val logoutUseCase: LogoutUseCase,
    private val authDataStore: AuthDataStore,
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState = _profileState.asStateFlow()

    init {
        authDataStore.getTokenFlow().filterNotNull().onEach {
            profileService.profile().collect { state ->
                when(state) {
                    is NetworkState.Result -> _profileState.value = ProfileState.Data(
                        state.data.username, state.data.email, state.data.avatar ?: "", state.data.enabled
                    )
                    is NetworkState.Error -> TODO()
                    NetworkState.Loading -> _profileState.value = ProfileState.Loading
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout() = viewModelScope.launch {
        logoutUseCase()
    }

}