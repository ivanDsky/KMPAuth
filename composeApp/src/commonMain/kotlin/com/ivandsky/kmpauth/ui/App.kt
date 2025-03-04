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
import com.ivandsky.kmpauth.navigation.ProfilesScreen
import com.ivandsky.kmpauth.navigation.RegisterScreen
import com.ivandsky.kmpauth.ui.auth.login.LoginScreen
import com.ivandsky.kmpauth.ui.auth.otp.OTPScreen
import com.ivandsky.kmpauth.ui.auth.register.RegisterScreen
import com.ivandsky.kmpauth.ui.profile.ProfileItem
import com.ivandsky.kmpauth.ui.profiles.ProfileListScreen
import com.ivandsky.kmpauth.util.serializableType
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import kotlin.reflect.typeOf

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(networkModule, serviceModule, viewModelModule, dataStoreModule, useCaseModule, navigationModule)
        }
    ) {
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
                is NavigationEvent.NavigateTo -> {
                    if(it.clearStack) {
                        navController.navigate(it.screen) {
                            navController.currentBackStackEntry?.destination?.route?.let { route ->
                                popUpTo(route) { inclusive =  true }
                            }
                        }
                    } else {
                        navController.navigate(it.screen)
                    }
                }
                NavigationEvent.PopBackStack -> {
                    navController.popBackStack()
                }
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
            com.ivandsky.kmpauth.ui.profile.ProfileScreen()
        }
        composable<ProfilesScreen> {
            ProfileListScreen()
        }
        composable<OTPScreen> {
            OTPScreen()
        }
    }
}