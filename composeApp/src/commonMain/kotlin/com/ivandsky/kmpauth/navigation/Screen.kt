package com.ivandsky.kmpauth.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.ivandsky.kmpauth.ui.profile.ProfileItem
import com.ivandsky.kmpauth.util.serializableType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

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
class ProfileScreen(val profileItem: ProfileItem = ProfileItem(id = -1)) : Screen {
    override val title: String = "Profile"

    companion object {
        val typeMap = mapOf(
            typeOf<ProfileItem>() to serializableType<ProfileItem>()
        )
        fun from(savedStateHandle: SavedStateHandle): ProfileScreen =
            savedStateHandle.toRoute(typeMap)
    }
}

@Serializable
data object ProfilesScreen : Screen {
    override val title: String = "All Profiles"
}

@Serializable
data object OTPScreen : Screen {
    override val title: String = "Verify"
}