package com.ivandsky.kmpauth.usecase

import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.navigation.LoginScreen
import com.ivandsky.kmpauth.navigation.NavigationEvent
import com.ivandsky.kmpauth.navigation.Navigator
import com.ivandsky.kmpauth.navigation.ProfileScreen
import com.ivandsky.kmpauth.navigation.ProfilesScreen
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.HttpResponseCancelled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AuthenticationWatcher(
    private val authDataStore: AuthDataStore,
    private val navigator: Navigator,
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    init {
        authDataStore.getTokenFlow().onEach {
            if(it == null) navigator.navigate(NavigationEvent.NavigateTo(LoginScreen, clearStack = true))
            else navigator.navigate(NavigationEvent.NavigateTo(ProfileScreen(), clearStack = true))
        }.launchIn(scope)
    }
}