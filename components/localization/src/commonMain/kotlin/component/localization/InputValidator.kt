package component.localization

interface InputValidator {
    fun validateEmail(email: String): String?
    fun validatePassword(password: String): String?
    fun validateRepeatPassword(password: String, repeatPassword: String): String?
    fun validateText(text: String, length: Int = 2): String?
    fun validatePhone(phone: String, length: Int = 8): String?
    fun validateNumberPositive(number: Int): String?
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

    override fun validateText(text: String, length: Int): String? {
        return when {
            text.isBlank() -> "Cannot be empty"
            text.length < length -> "Must be at least $length characters"
            else -> null
        }
    }

    override fun validatePhone(phone: String, length: Int): String? {
        return when {
            phone.isBlank() -> "Phone cannot be empty"
            phone.length < length -> "Phone must be at least 8 characters"
            else -> null
        }
    }

    override fun validateNumberPositive(number: Int): String? {
        return when {
            number < 0 -> "Number must be positive"
            else -> null
        }
    }
}
