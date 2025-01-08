package com.ivandsky.kmpauth.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
) {
    val data by viewModel.profileData.collectAsState()
    val characterData by viewModel.characterData.collectAsState()
    ProfileContainer(
        profileData = data,
        characterData = characterData,
        modifier = modifier,
        generateNewCharacter = viewModel::generateCharacter
    )
}

@Composable
fun ProfileContainer(
    profileData: ProfileData,
    characterData: WebData,
    modifier: Modifier = Modifier,
    generateNewCharacter: () -> Unit = {},
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Name: ${profileData.name}")
        Text(text = "Email: ${profileData.email}")
        Text(text = "Is verified? ${
            if(profileData.isVerified) "Yes!" else "No("
        }")

        Text(text = "Id: #${characterData.id}")
        Text(text = "Name: ${characterData.name}")
        AsyncImage(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
            ,
            model = characterData.image,
            contentDescription = null,
        )

        Button(onClick = generateNewCharacter) {
            Text("Generate new character")
        }
    }
}