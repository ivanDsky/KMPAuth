package com.ivandsky.kmpauth.util

sealed interface NetworkState<out T> {
    data class Result<T>(val data: T) : NetworkState<T>
    data class Error(val error: String) : NetworkState<Nothing>
    data object Loading : NetworkState<Nothing>
}