package component.localization

import component.localization.languages.englishLanguage

internal sealed class SupportedLanguage(val code: String) {
    data object English : SupportedLanguage("en")

    companion object {
        fun currentLanguage(): SupportedLanguage {
            return when (getDeviceLanguageCode()) {
                English.code -> English
                else -> English
            }
        }
    }
}

internal object StringProvider {
    private val currentLanguage: SupportedLanguage
        get() = SupportedLanguage.currentLanguage()

    // "My name is Adrian and i have 3 cars." -> "My name is %0 and i have %1 cars."
    operator fun invoke(key: Strings, params: Array<out Any>): String {
        val stringTemplate = when (currentLanguage) {
            SupportedLanguage.English -> englishLanguage(key)
        }

        return formatString(stringTemplate, params)
    }

    private fun formatString(template: String, params: Array<out Any>): String {
        var result = template
        params.forEachIndexed { index, param ->
            result = result.replace("%$index", param.toString())
        }
        return result
    }
}

internal expect fun getDeviceLanguageCode(): String

fun getString(key: Strings, vararg params: Any): String = StringProvider.invoke(key, params)
