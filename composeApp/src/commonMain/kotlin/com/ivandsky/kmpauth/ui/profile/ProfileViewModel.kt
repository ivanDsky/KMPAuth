package com.ivandsky.kmpauth.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ivandsky.kmpauth.data.auth.ProfileResponse
import com.ivandsky.kmpauth.data.auth.ProfileService
import com.ivandsky.kmpauth.navigation.NavigationEvent
import com.ivandsky.kmpauth.navigation.Navigator
import com.ivandsky.kmpauth.navigation.ProfileScreen
import com.ivandsky.kmpauth.navigation.ProfilesScreen
import com.ivandsky.kmpauth.usecase.LogoutUseCase
import com.ivandsky.kmpauth.util.NetworkState
import com.ivandsky.kmpauth.util.serializableType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class ProfileItem(
    val id: Long = -1,
    val username: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val role: String = "",
    val isVerified: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

fun ProfileItem.isAdmin(): Boolean = role == "Admin"

class ProfileViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val profileService: ProfileService,
    private val navigator: Navigator,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileItem>(ProfileItem(isLoading = true))
    val profileState = _profileState.asStateFlow()

    init {
        val data = ProfileScreen.from(savedStateHandle)
        if(data.profileItem.id != -1L) {
            _profileState.value = data.profileItem
        } else {
            profileService.profile().onEach { state ->
                when(state) {
                    is NetworkState.Result -> _profileState.update {
                        it.copy(
                            id = state.data.id,
                            username = state.data.username,
                            email = state.data.email,
                            role = state.data.roles.first(),
                            avatarUrl = state.data.avatar ?: "",
                            isVerified = state.data.enabled,
                            isLoading = false,
                            error = null
                        )
                    }
                    is NetworkState.Error -> _profileState.update {
                        it.copy(isLoading = false, error = state.error)
                    }
                    NetworkState.Loading -> _profileState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun navigateToProfiles() = viewModelScope.launch {
        navigator.navigate(NavigationEvent.NavigateTo(ProfilesScreen))
    }

    fun logout() = viewModelScope.launch {
        logoutUseCase()
    }

}