package com.ivandsky.kmpauth.di

import com.ivandsky.kmpauth.network.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
}