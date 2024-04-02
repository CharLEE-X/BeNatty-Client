package core.util

fun String.enumCapitalized() = this
    .split("(?=[A-Z])".toRegex())
    .dropWhile { it.isBlank() }
    .mapIndexed { index, word -> if (index != 0) word.lowercase() else word }
    .joinToString(" ") { it }
