package com.ivandsky.kmpauth.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivandsky.kmpauth.data.auth.AuthService
import com.ivandsky.kmpauth.data.auth.LoginRequest
import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.navigation.NavigationEvent
import com.ivandsky.kmpauth.navigation.Navigator
import com.ivandsky.kmpauth.navigation.OTPScreen
import com.ivandsky.kmpauth.navigation.ProfileScreen
import com.ivandsky.kmpauth.navigation.RegisterScreen
import com.ivandsky.kmpauth.util.NetworkState
import com.ivandsky.kmpauth.util.validation.PasswordValidator
import com.ivandsky.kmpauth.util.validation.UsernameEmailValidator
import com.ivandsky.kmpauth.util.validation.ValidationState
import com.ivandsky.kmpauth.util.validation.ValidationStateImpl
import com.ivandsky.kmpauth.util.validation.isValid
import com.mmk.kmpauth.google.GoogleUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginData(
    val usernameEmail: ValidationState<String> = ValidationStateImpl(""),
    val password: ValidationState<String> = ValidationStateImpl(""),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class LoginViewModel(
    private val authService: AuthService,
    private val authDataStore: AuthDataStore,
    private val navigator: Navigator,
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginData())
    val loginState = _loginState.asStateFlow()

    fun onEmailChanged(email: String) = viewModelScope.launch {
        _loginState.update { it.copy(usernameEmail = UsernameEmailValidator.validate(email)) }
    }

    fun onPasswordChanged(password: String) = viewModelScope.launch {
        _loginState.update { it.copy(password = PasswordValidator.validate(password)) }
    }

    fun onGoogleSignIn(googleUser: GoogleUser?) {
        viewModelScope.launch {
            googleUser?.idToken?.let { authDataStore.saveToken(it) }
        }
    }

    fun navigateToOTP() = viewModelScope.launch {
        navigator.navigate(NavigationEvent.NavigateTo(OTPScreen))
    }

    fun navigateToRegister() = viewModelScope.launch {
        navigator.navigate(NavigationEvent.NavigateTo(RegisterScreen, clearStack = true))
    }

    fun navigateToProfile() = viewModelScope.launch {
        navigator.navigate(NavigationEvent.NavigateTo(ProfileScreen(), clearStack = true))
    }

    private suspend fun validateAll(): Boolean {
        _loginState.update {
            it.copy(
                usernameEmail = UsernameEmailValidator.validate(it.usernameEmail.field),
                password = PasswordValidator.validate(it.password.field)
            )
        }
        return _loginState.value.let { it.usernameEmail.isValid && it.password.isValid }
    }

    fun login() = viewModelScope.launch{
        if(!validateAll()) return@launch
        authService.login(
            LoginRequest(loginState.value.usernameEmail.field, loginState.value.password.field)
        ).onEach { state ->
            when(state) {
                is NetworkState.Result -> {
                    _loginState.update { it.copy(isLoading = false) }
                    authDataStore.saveToken(state.data.token)
                    navigateToProfile()
                }
                is NetworkState.Error -> {
                    _loginState.update { it.copy(isLoading = false, error = state.error) }
                    if (state.error.contains("Account not verified")) {
                        authDataStore.saveEmail(_loginState.value.usernameEmail.field)
                        navigateToOTP()
                    }
                }
                NetworkState.Loading -> _loginState.update { it.copy(isLoading = true) }
            }
        }.launchIn(viewModelScope)
    }
}