package core.util

fun String.enumCapitalized() = lowercase()
    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
