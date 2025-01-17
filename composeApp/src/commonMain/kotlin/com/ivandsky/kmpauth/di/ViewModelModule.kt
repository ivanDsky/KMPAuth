package com.ivandsky.kmpauth.di

import com.ivandsky.kmpauth.ui.auth.login.LoginViewModel
import com.ivandsky.kmpauth.ui.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
}