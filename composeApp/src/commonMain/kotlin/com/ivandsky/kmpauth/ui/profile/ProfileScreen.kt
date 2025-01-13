package com.ivandsky.kmpauth.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.profileState.collectAsState()
    ProfileContainer(
        profileState = state,
        modifier = modifier
    )
}

@Composable
fun ProfileContainer(
    profileState: ProfileState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        when(profileState) {
            is ProfileState.Data -> {
                Text(text = "Name: ${profileState.name}")
                Text(text = "Email: ${profileState.email}")
                Text(text = "Is verified? ${
                    if(profileState.isVerified) "Yes!" else "No("
                }")
                AsyncImage(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                    ,
                    model = profileState.avatarUrl,
                    contentDescription = null,
                )
            }
            ProfileState.Loading -> CircularProgressIndicator()
        }
    }
}