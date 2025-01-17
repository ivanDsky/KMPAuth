package com.ivandsky.kmpauth.di

import com.ivandsky.kmpauth.local.AuthDataStore
import com.ivandsky.kmpauth.local.createDataStore
import org.koin.dsl.module

val dataStoreModule = module {
    single { createDataStore() }
    single { AuthDataStore(get()) }
}