package com.ivandsky.kmpauth.navigation

import androidx.navigation.NavOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface NavigationEvent {
    class NavigateTo(
        val screen: Screen,
        val clearStack: Boolean = false,
        val navOptions: NavOptions? = null
    ) : NavigationEvent
}

class Navigator {
    private val _navigationEvents = Channel<NavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    suspend fun navigate(event: NavigationEvent) {
        _navigationEvents.send(event)
    }
}