package com.ivandsky.kmpauth.navigation

import kotlinx.serialization.Serializable

interface Screen {
    val title: String
}

@Serializable
data object LoginScreen : Screen {
    override val title: String = "Login"
}

@Serializable
data object RegisterScreen : Screen {
    override val title: String = "Register"
}

@Serializable
class ProfileScreen() : Screen {
    override val title: String = "Profile"
}

@Serializable
data object ProfilesScreen : Screen {
    override val title: String = "All Profiles"
}

@Serializable
data object OTPScreen : Screen {
    override val title: String = "Verify"
}