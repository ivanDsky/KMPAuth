package com.ivandsky.kmpauth.util.validation

interface ValidationState<T> {
    val field: T
    val error: String?
}

val ValidationState<*>.isValid: Boolean
    get() = error == null

internal data class ValidationStateImpl<T> (
    override val field: T,
    override val error: String? = null
) : ValidationState<T>

interface Validator<T> {
    suspend fun validate(input: T) : ValidationState<T>
}

