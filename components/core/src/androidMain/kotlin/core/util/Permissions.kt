package core.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import co.touchlab.kermit.Logger
import core.models.Permission
import core.models.PermissionStatus
import org.koin.core.error.NoDefinitionFoundException

/**
 * Opens the settings page for the given permission.
 */
fun Context.openPage(
    action: String,
    newData: Uri? = null,
    onError: (Exception) -> Unit,
) {
    try {
        val intent = Intent(action).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            newData?.let { data = it }
        }
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        onError(e)
    }
}

/**
 * Checks the given permissions.
 */
fun checkPermissions(
    context: Context,
    activity: Lazy<Activity>,
    logger: Logger,
    permissions: List<String>,
): PermissionStatus {
    permissions.ifEmpty { return PermissionStatus.GRANTED } // no permissions needed
    val status: List<Int> = permissions.map {
        context.checkSelfPermission(it)
    }
    val isAllGranted: Boolean = status.all { it == PackageManager.PERMISSION_GRANTED }
    if (isAllGranted) return PermissionStatus.GRANTED

    // SDK starts checking permissions before activity is available, so until we have activity
    // we can't check permission rationale, so we return NOT_DETERMINED. It makes not difference
    // for AppState, because permission is not granted anyway. Code below matters only in the UI,
    // but the Activity is ready at this point.
    val isAllRequestRationale: Boolean = try {
        permissions.all {
            !activity.value.shouldShowRequestPermissionRationale(it)
        }
    } catch (e: ActivityNotFoundException) {
        logger.e { "Failed to check permission rationale. No Activity." }
        true
    } catch (e: NoDefinitionFoundException) {
        logger.e { "Failed to check permission rationale. No Activity." }
        true
    }
    return if (isAllRequestRationale) PermissionStatus.NOT_DETERMINED
    else PermissionStatus.DENIED
}

internal const val REQUEST_CODE = 100

/**
 * Requests the given permissions.
 */
fun Activity.providePermissions(
    permissions: List<String>,
    onError: (Throwable) -> Unit,
) {
    try {
        ActivityCompat.requestPermissions(
            this, permissions.toTypedArray(), REQUEST_CODE,
        )
    } catch (e: ActivityNotFoundException) {
        onError(e)
    }
}

/**
 * Opens the settings page for the given permission.
 */
fun Context.openAppSettingsPage(permission: Permission) {
    openPage(
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        newData = Uri.parse("package:$packageName"),
        onError = { throw Exception("Cannot open settings for permission ${permission.name}.") },
    )
}
