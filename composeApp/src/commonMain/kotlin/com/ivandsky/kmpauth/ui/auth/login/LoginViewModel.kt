package com.ivandsky.kmpauth.ui.auth.login

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivandsky.kmpauth.data.auth.AuthService
import com.ivandsky.kmpauth.data.auth.LoginRequest
import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class LoginData(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)

class LoginViewModel(
    private val authService: AuthService,
    private val authDataStore: AuthDataStore,
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginData())
    val loginState = _loginState.asStateFlow()

    init {
        authDataStore.getTokenFlow().filterNotNull().onEach { token ->
            _loginState.update { it.copy(email = token) }
        }.launchIn(viewModelScope)
    }

    fun onEmailChanged(email: String) {
        _loginState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _loginState.update { it.copy(password = password) }
    }

    fun login() {
        authService.login(
            LoginRequest(loginState.value.email, loginState.value.password)
        ).onEach { s ->
            when(s) {
                is NetworkState.Result -> {
                    _loginState.update { it.copy(isLoading = false) }
                    authDataStore.saveToken(s.data.token)
                }
                is NetworkState.Error -> TODO()
                NetworkState.Loading -> _loginState.update { it.copy(isLoading = true) }
            }
        }.launchIn(viewModelScope)
    }
}