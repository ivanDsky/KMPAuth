package com.ivandsky.kmpauth.navigation

import kotlinx.serialization.Serializable

interface Screen

@Serializable
data object LoginScreen : Screen

@Serializable
data object RegisterScreen : Screen

@Serializable
data object ProfileScreen : Screen

@Serializable
data object OTPScreen : Screen