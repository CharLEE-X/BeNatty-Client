package core.util

fun String.enumCapitalized() = this.split("(?=[A-Z])".toRegex())
    .joinToString(" ") { it.lowercase() }
    .replaceFirstChar(Char::titlecase)
