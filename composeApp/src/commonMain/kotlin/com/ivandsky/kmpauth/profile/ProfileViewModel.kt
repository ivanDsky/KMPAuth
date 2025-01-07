package com.ivandsky.kmpauth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileData(
    val name: String,
    val email: String,
    val isVerified: Boolean,
)

class ProfileViewModel(
    private val httpClient: HttpClient,
) : ViewModel() {
    private val _profileData = MutableStateFlow(
        ProfileData(
            name = "Ivan",
            email = "ivan.dobrovolskyi@thoughtworks.com",
            isVerified = false
        )
    )
    val profileData = _profileData.asStateFlow()

    init {
        viewModelScope.launch {
            _profileData.update { it.copy(email = getUrl()) }
            var ctr = 0
            while(true) {
                ctr++
                delay(1000)
                _profileData.update { it.copy(name = "Ivan $ctr") }
            }
        }
    }

    private suspend fun getUrl(): String {
        val response = httpClient.get("https://www.google.com/")
        return response.bodyAsText()
    }
}