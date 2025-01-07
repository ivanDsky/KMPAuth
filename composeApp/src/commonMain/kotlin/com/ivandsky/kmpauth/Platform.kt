package com.ivandsky.kmpauth

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform