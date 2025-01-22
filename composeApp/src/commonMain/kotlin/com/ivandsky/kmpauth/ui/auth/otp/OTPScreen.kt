package com.ivandsky.kmpauth.ui.auth.otp

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ivandsky.kmpauth.ui.common.ErrorMessage
import com.ivandsky.kmpauth.ui.common.GradientButton
import org.koin.compose.viewmodel.koinViewModel

// Composable Screen
@Composable
fun OTPScreen(
    viewModel: OTPViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                )
            )
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
                .shadow(24.dp, shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Verify Your Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Subtitle
                Text(
                    text = "We've sent a 6-digit code to your email",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // OTP Input
                OTPTextField(
                    otp = state.otp,
                    onOtpChange = viewModel::onOTPChange,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Error Message
                state.error?.let { error ->
                    ErrorMessage(
                        text = error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Verify Button
                GradientButton(
                    text = "Verify",
                    isLoading = state.isLoading,
                    onClick = {
                        viewModel.verifyOTP()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Resend OTP Section
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Didn't receive code? ",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    TextButton(
                        onClick = viewModel::resendOTP,
                        enabled = state.resendEnabled,
                    ) {
                        Text(
                            text = if(state.resendEnabled) "Resend Now" else "Resend in ${state.countdown}s",
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun OTPTextField(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = otp,
            onValueChange = onOtpChange,
            modifier = Modifier
                .onFocusChanged { if (it.isFocused) keyboardController?.show() }
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.alpha(0f)) {
                    innerTextField()
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(6) { index ->
                        val char = otp.getOrNull(index)
                        val isFocused = otp.length == index

                        OTPDigitBox(
                            char = char,
                            isFocused = isFocused,
                            onClick = { focusRequester.requestFocus() }
                        )

                        if (index < 5) Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        )
    }
}

@Composable
fun OTPDigitBox(
    char: Char?,
    isFocused: Boolean,
    onClick: () -> Unit
) {
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline,
        label = "borderColor"
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .border(
                width = 2.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char?.toString() ?: "",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}