package com.ivandsky.kmpauth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ivandsky.kmpauth.di.dataStoreModule
import com.ivandsky.kmpauth.di.networkModule
import com.ivandsky.kmpauth.di.serviceModule
import com.ivandsky.kmpauth.di.viewModelModule
import com.ivandsky.kmpauth.ui.auth.login.LoginScreen
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(networkModule, serviceModule, viewModelModule, dataStoreModule)
        }
    ) {
        GoogleAuthProvider.create(credentials =
            GoogleAuthCredentials(
                serverId = "125815290751-5ob84urrher3sab4nvp6u2f3j50rcnf6.apps.googleusercontent.com"
            )
        )

        MaterialTheme {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                LoginScreen()
            }
        }
    }
}