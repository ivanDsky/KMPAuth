package com.ivandsky.kmpauth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ivandsky.kmpauth.di.dataStoreModule
import com.ivandsky.kmpauth.di.networkModule
import com.ivandsky.kmpauth.di.serviceModule
import com.ivandsky.kmpauth.di.useCaseModule
import com.ivandsky.kmpauth.di.viewModelModule
import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.ui.auth.login.LoginScreen
import com.ivandsky.kmpauth.ui.profile.ProfileScreen
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
            modules(networkModule, serviceModule, viewModelModule, dataStoreModule, useCaseModule)
        }
    ) {
        GoogleAuthProvider.create(credentials =
            GoogleAuthCredentials(
                serverId = "125815290751-5ob84urrher3sab4nvp6u2f3j50rcnf6.apps.googleusercontent.com"
            )
        )

        val authDataStore = koinInject<AuthDataStore>()
        val token by authDataStore.getTokenFlow().collectAsState(null)

        MaterialTheme {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                if(token == null) {
                    LoginScreen()
                } else {
                    ProfileScreen()
                }
            }
        }
    }
}