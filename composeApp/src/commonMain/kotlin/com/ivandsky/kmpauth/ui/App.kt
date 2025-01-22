package com.ivandsky.kmpauth.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ivandsky.kmpauth.di.dataStoreModule
import com.ivandsky.kmpauth.di.navigationModule
import com.ivandsky.kmpauth.di.networkModule
import com.ivandsky.kmpauth.di.serviceModule
import com.ivandsky.kmpauth.di.useCaseModule
import com.ivandsky.kmpauth.di.viewModelModule
import com.ivandsky.kmpauth.navigation.LoginScreen
import com.ivandsky.kmpauth.navigation.NavigationEvent
import com.ivandsky.kmpauth.navigation.Navigator
import com.ivandsky.kmpauth.navigation.OTPScreen
import com.ivandsky.kmpauth.navigation.ProfileScreen
import com.ivandsky.kmpauth.navigation.RegisterScreen
import com.ivandsky.kmpauth.ui.auth.login.LoginScreen
import com.ivandsky.kmpauth.ui.auth.otp.OTPScreen
import com.ivandsky.kmpauth.ui.auth.register.RegisterScreen
import com.ivandsky.kmpauth.ui.profile.ProfileScreen
import com.ivandsky.kmpauth.usecase.AuthenticationWatcher
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(networkModule, serviceModule, viewModelModule, dataStoreModule, useCaseModule, navigationModule)
        }
    ) {
        val authenticationWatcher = koinInject<AuthenticationWatcher>()

        GoogleAuthProvider.create(credentials =
            GoogleAuthCredentials(
                serverId = "125815290751-5ob84urrher3sab4nvp6u2f3j50rcnf6.apps.googleusercontent.com"
            )
        )

        Navigator(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun Navigator(
    modifier: Modifier = Modifier
) {
    val navigator = koinInject<Navigator>()
    val navController = rememberNavController()

    LaunchedEffect(navigator, navController) {
        navigator.navigationEvents.collect {
            when(it) {
                is NavigationEvent.NavigateTo -> navController.navigate(it.screen)
            }
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = LoginScreen
    ) {
        composable<LoginScreen> {
            LoginScreen()
        }
        composable<RegisterScreen> {
            RegisterScreen()
        }
        composable<ProfileScreen> {
            ProfileScreen()
        }
        composable<OTPScreen> {
            OTPScreen()
        }
    }
}