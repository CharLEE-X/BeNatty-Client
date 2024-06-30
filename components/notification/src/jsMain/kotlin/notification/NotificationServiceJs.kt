package notification

import co.touchlab.kermit.Logger
import core.models.PermissionStatus

internal class NotificationServiceJs(private val logger: Logger) : NotificationService {
    override suspend fun checkPermission(): PermissionStatus {
        return PermissionStatus.NOT_DETERMINED
    }

    override suspend fun requestPermission() {
        logger.v { "Requesting notifications permission" }
    }

    override fun openSettingPage() {
        logger.v { "Opening notification settings page" }
    }

    override suspend fun schedule(notificationType: NotificationType) {
        logger.v { "Scheduling notification: $notificationType" }
    }

    override suspend fun cancelNotification(ids: List<String>) {
        logger.v { "Cancelling notification with IDs: [${ids.joinToString()}]" }
    }
}
