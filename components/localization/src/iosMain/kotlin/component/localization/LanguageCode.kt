package component.localization

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

@Suppress("unused")
internal actual fun getDeviceLanguageCode(): String {
    return NSLocale.currentLocale.languageCode
}
