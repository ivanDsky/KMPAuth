package com.ivandsky.kmpauth.di

import com.ivandsky.kmpauth.ui.auth.login.LoginViewModel
import com.ivandsky.kmpauth.ui.auth.otp.OTPViewModel
import com.ivandsky.kmpauth.ui.auth.register.RegisterViewModel
import com.ivandsky.kmpauth.ui.profile.ProfileViewModel
import com.ivandsky.kmpauth.ui.profiles.ProfilesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get()) }
    viewModel { OTPViewModel(get(), get(), get()) }
    viewModel { ProfilesViewModel(get(), get()) }
}