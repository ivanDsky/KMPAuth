package com.ivandsky.kmpauth.util.validation


object NotBlankValidator : Validator<String> {
    override suspend fun validate(input: String): ValidationState<String> {
        return if(input.isNotBlank()) ValidationStateImpl(input)
        else ValidationStateImpl(input, "Input can't be empty")
    }
}

class NotContainsValidator(private val forbiddenCharacters: String) : Validator<String> {
    private val forbiddenCharactersDescription =
        forbiddenCharacters.toList().joinToString(",", prefix = "[", postfix = "]")
    override suspend fun validate(input: String): ValidationState<String> {
        if(input.any { it in forbiddenCharacters }) return ValidationStateImpl(input, "Input can't contain $forbiddenCharactersDescription")
        return ValidationStateImpl(input)
    }
}

class RangeValidator(private val range: IntRange) : Validator<String> {
    override suspend fun validate(input: String): ValidationState<String> {
        if(input.length in range) return ValidationStateImpl(input)
        if(input.length < range.first) return ValidationStateImpl(input, "Input should be at least ${range.first} characters")
        return ValidationStateImpl(input, "Input should be at most ${range.last} characters")
    }
}

object EmailValidator : Validator<String> {
    override suspend fun validate(input: String): ValidationState<String> {
        return if(emailRegex.matches(input)) ValidationStateImpl(input)
        else ValidationStateImpl(input, "Incorrect email address")
    }

    private val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
}

object PasswordValidator : Validator<String> {
    override suspend fun validate(input: String): ValidationState<String> {
        if(NotBlankValidator.validate(input).error != null) return ValidationStateImpl(input, "Password can't be empty")
        if(RangeValidator(4..64).validate(input).error != null) return ValidationStateImpl(input, "Password should be at least 4 characters")
        return ValidationStateImpl(input)
    }
}

object UsernameValidator : Validator<String> {
    override suspend fun validate(input: String): ValidationState<String> {
        if(NotBlankValidator.validate(input).error != null) return ValidationStateImpl(input, "Username can't be empty")
        if(NotContainsValidator("@").validate(input).error != null) return ValidationStateImpl(input, "Username can't contain \'@\'")
        return ValidationStateImpl(input)
    }
}

object UsernameEmailValidator : Validator<String> {
    override suspend fun validate(input: String): ValidationState<String> {
        return if(input.contains('@')) EmailValidator.validate(input)
        else UsernameValidator.validate(input)
    }
}

class PasswordConfirmationValidator(private val password: String) : Validator<String> {
    override suspend fun validate(input: String): ValidationState<String> {
        if(input != password) return ValidationStateImpl(input, "Passwords are different")
        return ValidationStateImpl(input)
    }
}

class OTPValidator(private val length: Int): Validator<String> {
    override suspend fun validate(input: String): ValidationState<String> {
        if(input.length != length) return ValidationStateImpl(input, "OTP code should be 6-digit number")
        if(input.any { !it.isDigit() }) return ValidationStateImpl(input, "OTP code contains only digits")
        return ValidationStateImpl(input)
    }

}