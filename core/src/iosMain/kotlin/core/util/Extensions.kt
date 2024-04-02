package core.util

import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

private const val CF_BUNDLE_SHORT_VERSION_STRING = "CFBundleShortVersionString"
private const val CF_BUNDLE_VERSION = "CFBundleVersion"
private const val CF_BUNDLE_NAME = "CFBundleName"

/**
 * Retrieves the version information from the info dictionary of an iOS bundle.
 *
 * @return An instance of BundleVersionInformation containing the retrieved version information,
 */
val NSBundle.versionInformation
    get() = infoDictionary?.let {
        BundleVersionInformation(
            version = it[CF_BUNDLE_SHORT_VERSION_STRING] as? String,
            build = it[CF_BUNDLE_VERSION] as? String,
            name = it[CF_BUNDLE_NAME] as? String
        )
    } ?: BundleVersionInformation(null, null, null)

/**
 * Constructs a new BundleVersionInformation object with the provided version information.
 *
 * @property name The name of the bundle.
 * @property version The short version string of the bundle.
 * @property build The build version of the bundle.
 */
data class BundleVersionInformation(
    val name: String?,
    val version: String?,
    val build: String?,
) {
    /**
     * Returns the version string if available; otherwise, returns the build version string.
     */
    val versionOrBuild = version ?: build
}

/**
 * Opens the app settings page.
 */
fun openAppSettingsPage() {
    openNSUrl(UIApplicationOpenSettingsURLString)
}

/**
 * Opens the provided URL string.
 */
fun openNSUrl(string: String) {
    val settingsUrl: NSURL = NSURL.URLWithString(string)!!
    if (UIApplication.sharedApplication.canOpenURL(settingsUrl)) {
        UIApplication.sharedApplication.openURL(settingsUrl)
    } else throw Exception("Cannot open settings: $settingsUrl")
}
