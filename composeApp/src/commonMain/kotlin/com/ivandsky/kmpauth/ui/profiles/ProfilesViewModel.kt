package com.ivandsky.kmpauth.ui.profiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivandsky.kmpauth.data.auth.ProfileService
import com.ivandsky.kmpauth.navigation.NavigationEvent
import com.ivandsky.kmpauth.navigation.Navigator
import com.ivandsky.kmpauth.navigation.ProfileScreen
import com.ivandsky.kmpauth.ui.profile.ProfileItem
import com.ivandsky.kmpauth.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileListState(
    val profiles: List<ProfileItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfilesViewModel(
    private val profileService: ProfileService,
    private val navigator: Navigator,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileListState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        profileService.allProfiles().onEach { state ->
            when(state) {
                is NetworkState.Result -> _state.value =
                    ProfileListState(
                        profiles = state.data.map {
                            ProfileItem(
                                id = it.id,
                                username = it.username,
                                email = it.email,
                                avatarUrl = it.avatar,
                                role = it.roles.first(),
                                isVerified = it.enabled,
                            )
                        },
                        isLoading = false,
                        error = null,
                    )
                is NetworkState.Error -> _state.update { it.copy(isLoading = false, error = state.error) }
                NetworkState.Loading -> _state.update { it.copy(isLoading = true) }
            }
        }.launchIn(viewModelScope)
    }

    fun onProfileSelected(profileItem: ProfileItem) = viewModelScope.launch {
        navigator.navigate(NavigationEvent.NavigateTo(ProfileScreen()))
    }
}