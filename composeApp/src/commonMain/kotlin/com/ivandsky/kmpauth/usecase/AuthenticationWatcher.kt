package com.ivandsky.kmpauth.usecase

import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.navigation.LoginScreen
import com.ivandsky.kmpauth.navigation.NavigationEvent
import com.ivandsky.kmpauth.navigation.Navigator
import com.ivandsky.kmpauth.navigation.ProfileScreen
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
            if(it == null) navigator.navigate(NavigationEvent.NavigateTo(LoginScreen))
            else navigator.navigate(NavigationEvent.NavigateTo(ProfileScreen))
        }.launchIn(scope)
    }
}