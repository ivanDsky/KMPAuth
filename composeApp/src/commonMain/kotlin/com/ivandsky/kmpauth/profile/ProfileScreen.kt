package com.ivandsky.kmpauth.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
) {
    val data by viewModel.profileData.collectAsState()
    ProfileContainer(
        profileData = data,
        modifier = modifier
    )
}

@Composable
fun ProfileContainer(
    profileData: ProfileData,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Name: ${profileData.name}")
        Text(text = "Email: ${profileData.email}")
        Text(text = "Is verified? ${
            if(profileData.isVerified) "Yes!" else "No("
        }")
    }
}