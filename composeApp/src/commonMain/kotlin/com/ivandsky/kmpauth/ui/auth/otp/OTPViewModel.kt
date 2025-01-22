package com.ivandsky.kmpauth.ui.auth.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivandsky.kmpauth.data.auth.AuthService
import com.ivandsky.kmpauth.data.auth.ValidationRequest
import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.navigation.LoginScreen
import com.ivandsky.kmpauth.navigation.NavigationEvent
import com.ivandsky.kmpauth.navigation.Navigator
import com.ivandsky.kmpauth.util.NetworkState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OTPVerificationState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val resendEnabled: Boolean = false,
    val countdown: Int = 30
)

class OTPViewModel(
    private val authService: AuthService,
    private val authDataStore: AuthDataStore,
    private val navigator: Navigator,
) : ViewModel() {
    private val _state = MutableStateFlow(OTPVerificationState())
    val state = _state.asStateFlow()

    private var timerJob: Job? = null

    init {
        startCountdown()
    }

    fun onOTPChange(newOtp: String) {
        if (newOtp.length <= 6 && newOtp.all { it.isDigit() }) {
            _state.update { it.copy(otp = newOtp, error = null) }
        }
    }

    fun verifyOTP() = viewModelScope.launch {
        if (_state.value.otp.length != 6) {
            _state.update { it.copy(error = "Please enter 6-digit code") }
            return@launch
        }

        _state.update { it.copy(isLoading = true, error = null) }

        val email = authDataStore.getEmail() ?: return@launch
        authService.validate(
            ValidationRequest(email, _state.value.otp)
        ).onEach { s ->
            when(s) {
                is NetworkState.Error -> _state.update { it.copy(isLoading = false, error = s.error) }
                NetworkState.Loading -> _state.update { it.copy(isLoading = true) }
                is NetworkState.Result -> {
                    _state.update { it.copy(isLoading = false) }
                    navigateToLogin()
                }
            }
        }.launchIn(viewModelScope)
    }

    fun resendOTP() = viewModelScope.launch{
        startCountdown()
        val email = authDataStore.getEmail() ?: return@launch
        authService.resend(
            email
        ).onEach { s ->
            when(s) {
                is NetworkState.Error -> _state.update { it.copy(error = s.error) }
                is NetworkState.Result -> {}
                is NetworkState.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun navigateToLogin() = viewModelScope.launch {
        navigator.navigate(NavigationEvent.NavigateTo(LoginScreen))
    }

    private fun startCountdown() {
        timerJob?.cancel()
        _state.update {
            it.copy(
                resendEnabled = false,
                countdown = 30
            )
        }

        timerJob = viewModelScope.launch {
            for (time in 29 downTo 0) {
                delay(1000)
                _state.update { it.copy(countdown = time) }
            }
            _state.update { it.copy(resendEnabled = true) }
        }
    }
}