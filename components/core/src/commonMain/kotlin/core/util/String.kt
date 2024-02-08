package core.util

fun String.enumCapitalized() = this.split("(?=[A-Z])".toRegex())
    .joinToString(" ") { it.lowercase() }
    .replaceFirstChar { if (it.isLowerCase()) it.uppercase() else it.toString() }
