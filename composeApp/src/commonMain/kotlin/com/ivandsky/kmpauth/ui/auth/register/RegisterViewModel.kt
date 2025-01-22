package com.ivandsky.kmpauth.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivandsky.kmpauth.data.auth.AuthService
import com.ivandsky.kmpauth.data.auth.RegisterRequest
import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.navigation.LoginScreen
import com.ivandsky.kmpauth.navigation.NavigationEvent
import com.ivandsky.kmpauth.navigation.Navigator
import com.ivandsky.kmpauth.navigation.OTPScreen
import com.ivandsky.kmpauth.util.NetworkState
import com.ivandsky.kmpauth.util.validation.EmailValidator
import com.ivandsky.kmpauth.util.validation.PasswordConfirmationValidator
import com.ivandsky.kmpauth.util.validation.PasswordValidator
import com.ivandsky.kmpauth.util.validation.UsernameValidator
import com.ivandsky.kmpauth.util.validation.ValidationState
import com.ivandsky.kmpauth.util.validation.ValidationStateImpl
import com.ivandsky.kmpauth.util.validation.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterData(
    val username: ValidationState<String> = ValidationStateImpl(""),
    val email: ValidationState<String> = ValidationStateImpl(""),
    val password: ValidationState<String> = ValidationStateImpl(""),
    val passwordConfirmation: ValidationState<String> = ValidationStateImpl(""),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class RegisterViewModel(
    private val authService: AuthService,
    private val authDataStore: AuthDataStore,
    private val navigator: Navigator,
) : ViewModel() {
    private val _registerState = MutableStateFlow(RegisterData())
    val registerState = _registerState.asStateFlow()

    fun onUsernameChanged(username: String) = viewModelScope.launch {
        _registerState.update { it.copy(username = UsernameValidator.validate(username)) }
    }

    fun onEmailChanged(email: String) = viewModelScope.launch {
        _registerState.update { it.copy(email = EmailValidator.validate(email)) }
    }

    fun onPasswordChanged(password: String) = viewModelScope.launch {
        _registerState.update { it.copy(password = PasswordValidator.validate(password)) }
    }

    fun onPasswordConfirmationChanged(confirmationPassword: String) = viewModelScope.launch {
        _registerState.update {
            it.copy(passwordConfirmation = PasswordConfirmationValidator(it.password.field).validate(confirmationPassword))
        }
    }

    fun navigateToLogin() = viewModelScope.launch {
        navigator.navigate(NavigationEvent.NavigateTo(LoginScreen))
    }

    fun navigateToOTP() = viewModelScope.launch {
        navigator.navigate(NavigationEvent.NavigateTo(OTPScreen))
    }

    private suspend fun validateAll(): Boolean {
        _registerState.update {
            it.copy(
                username = UsernameValidator.validate(it.username.field),
                email = EmailValidator.validate(it.email.field),
                password = PasswordValidator.validate(it.password.field),
                passwordConfirmation = PasswordConfirmationValidator(it.password.field)
                    .validate(it.passwordConfirmation.field)
            )
        }
        return _registerState.value.let {
            it.username.isValid && it.email.isValid && it.password.isValid && it.passwordConfirmation.isValid
        }
    }

    fun register() = viewModelScope.launch{
        if(!validateAll()) return@launch
        authService.register(
            RegisterRequest(
                username = registerState.value.username.field,
                email = registerState.value.email.field,
                password = registerState.value.password.field
            )
        ).onEach { s ->
            when(s) {
                is NetworkState.Result -> {
                    authDataStore.saveEmail(registerState.value.email.field)
                    _registerState.update { it.copy(isLoading = false) }
                    navigateToOTP()
                }
                is NetworkState.Error -> _registerState.update { it.copy(isLoading = false, error = s.error) }
                NetworkState.Loading -> _registerState.update { it.copy(isLoading = true) }
            }
        }.launchIn(viewModelScope)
    }
}