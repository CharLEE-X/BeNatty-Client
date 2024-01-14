package component.localization

import java.util.Locale

@Suppress("unused")
internal actual fun getDeviceLanguageCode(): String {
    return Locale.getDefault().language
}
