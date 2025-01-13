package com.ivandsky.kmpauth.ui.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val loginData by viewModel.loginState.collectAsState()
    LoginContainer(
        modifier = modifier,
        loginData = loginData,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onLogin = viewModel::login
    )
}

@Composable
private fun LoginContainer(
    loginData: LoginData,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isInputBlocked = loginData.isLoading
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = loginData.email, onValueChange = onEmailChange, enabled = !isInputBlocked)
        TextField(value = loginData.password, onValueChange = onPasswordChange, enabled = !isInputBlocked)
        Button(onClick = onLogin, enabled = !isInputBlocked) {
            if(loginData.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Sign in")
            }
        }
    }
}