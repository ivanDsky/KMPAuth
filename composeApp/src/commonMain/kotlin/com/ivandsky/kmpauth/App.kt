package com.ivandsky.kmpauth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivandsky.kmpauth.network.httpClientModule
import com.ivandsky.kmpauth.profile.ProfileData
import com.ivandsky.kmpauth.profile.ProfileScreen
import com.ivandsky.kmpauth.profile.ProfileViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmpauth.composeapp.generated.resources.Res
import kmpauth.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.KoinApplication
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ProfileViewModel(get()) }
}

@Composable
@Preview
fun App() {
    KoinApplication(
        application = {
            modules(viewModelModule, httpClientModule)
        }
    ) {
        MaterialTheme {
            var showContent by remember { mutableStateOf(false) }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Hello from Ivan",
                    modifier = Modifier.padding(50.dp)
                )
                Button(onClick = { showContent = !showContent }) {
                    Text("Click me!")
                }
                AnimatedVisibility(showContent) {
                    ProfileScreen(
                        viewModel = koinViewModel(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}