package notification

import core.models.PermissionStatus

interface NotificationService {
    suspend fun checkPermission(): PermissionStatus
    suspend fun requestPermission()
    fun openSettingPage()
    suspend fun schedule(notificationType: NotificationType)
    suspend fun cancelNotification(ids: List<String>)
}
