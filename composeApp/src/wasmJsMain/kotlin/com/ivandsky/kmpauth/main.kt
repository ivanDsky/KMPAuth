package com.ivandsky.kmpauth

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.ivandsky.kmpauth.ui.App
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        App()
    }
}