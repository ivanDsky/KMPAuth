package com.ivandsky.kmpauth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.random.Random

data class ProfileData(
    val name: String,
    val email: String,
    val isVerified: Boolean,
)

@Serializable
data class WebData(
    val id: Int,
    val name: String,
    val image: String
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
    private val _characterData = MutableStateFlow(
        WebData(
            id = -1,
            name = "Loading",
            image = ""
        )
    )
    val characterData = _characterData.asStateFlow()

    init {
        viewModelScope.launch {
            var ctr = 0
            while(true) {
                ctr++
                delay(1000)
                _profileData.update { it.copy(name = "Ivan $ctr") }
            }
        }
    }

    fun generateCharacter() = viewModelScope.launch {
        _characterData.value = getUrl(Random.nextInt(1,800))
    }

    private suspend fun getUrl(id: Int): WebData {
        val response = httpClient.get("https://rickandmortyapi.com/api/character/$id")
        return response.body()
    }
}