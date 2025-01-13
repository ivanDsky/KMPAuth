package com.ivandsky.kmpauth

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ivandsky.kmpauth.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMPAuth",
    ) {
        App()
    }
}