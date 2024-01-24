package component.localization

interface InputValidator {
    fun validateEmail(email: String): String?
    fun validatePassword(password: String): String?
    fun validateRepeatPassword(password: String, repeatPassword: String): String?
    fun validateName(name: String): String?
}

internal class InputValidatorImpl : InputValidator {
    override fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty"
            email.length < 8 -> "Email must be at least 6 characters"
            "@" !in email -> "Email is invalid"
            else -> null
        }
    }

    override fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    override fun validateRepeatPassword(password: String, repeatPassword: String): String? {
        return when {
            password != repeatPassword -> "Passwords do not match"
            else -> null
        }
    }

    override fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Name cannot be empty"
            name.length < 2 -> "Name must be at least 2 characters"
            else -> null
        }
    }
}
