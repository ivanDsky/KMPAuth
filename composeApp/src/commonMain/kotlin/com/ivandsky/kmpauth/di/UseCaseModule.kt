package com.ivandsky.kmpauth.di

import com.ivandsky.kmpauth.usecase.AuthenticationWatcher
import com.ivandsky.kmpauth.usecase.LogoutUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { LogoutUseCase(get(), get()) }
    single(createdAtStart = true) { AuthenticationWatcher(get(), get()) }
}