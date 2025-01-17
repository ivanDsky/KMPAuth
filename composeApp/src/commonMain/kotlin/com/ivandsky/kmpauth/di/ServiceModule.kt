package com.ivandsky.kmpauth.di

import com.ivandsky.kmpauth.data.auth.AuthService
import com.ivandsky.kmpauth.data.auth.ProfileService
import org.koin.dsl.module

val serviceModule = module {
    single { AuthService(get()) }
    single { ProfileService(get(), get()) }
}