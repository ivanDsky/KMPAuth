package com.ivandsky.kmpauth.di

import com.ivandsky.kmpauth.navigation.Navigator
import org.koin.dsl.module

val navigationModule = module {
    single { Navigator() }
}