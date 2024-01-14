package notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.RemoteException
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import co.touchlab.kermit.Logger
import core.models.Permission
import core.models.PermissionStatus
import core.util.checkPermissions
import core.util.openAppSettingsPage
import notification.util.IdentifiableIntent
import java.lang.System.currentTimeMillis
import kotlin.random.Random

internal class NotificationServiceAndroid(
    private val logger: Logger,
    private val context: Context,
    private val activity: Lazy<Activity>,
    private val notificationManager: NotificationManager,
    private val alarmManager: AlarmManager,
) : NotificationService {
    private val localNotificationPermissions: List<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.POST_NOTIFICATIONS)
        } else emptyList()

    init {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "General", importance)
        channel.description = "General"
        notificationManager.createNotificationChannel(channel)
    }

    override suspend fun checkPermission(): PermissionStatus {
        return checkPermissions(
            context = context,
            activity = activity,
            logger = logger,
            permissions = localNotificationPermissions,
        )
    }

    override suspend fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.value.requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE,
            )
        }
    }

    override fun openSettingPage() {
        context.openAppSettingsPage(Permission.LOCAL_NOTIFICATIONS)
    }


    @SuppressLint("MissingPermission")
    override suspend fun schedule(notificationType: NotificationType) {
        if (checkPermission().isNotGranted()) {
            requestPermission()
            return
        }

        when (notificationType) {
            is NotificationType.Immediate -> {
                val id = Random.nextInt()
                val notification = sendNotification(
                    title = notificationType.title,
                    body = notificationType.body,
                )
                notificationManager.notify(id, notification)
            }

            is NotificationType.Scheduled -> {}
        }
    }

    override suspend fun cancelNotification(ids: List<String>) {
        if (ids.isEmpty()) return
        ids.forEach { notificationId ->
            try {
                logger.v { "Cancelling pending notification with ID: $notificationId" }
                alarmManager.cancel(createPendingIntent(notificationId.length))
                notificationManager.cancel(notificationId.toInt())
            } catch (e: RemoteException) {
                logger.w(e) { "Couldn't cancel notification with ID '$notificationId'." }
            } catch (e: NumberFormatException) {
                logger.w(e) { "Couldn't find notification with ID '$notificationId'." }
            }
        }
    }

    @SuppressLint("MissingPermission") // Permission checked in "schedule" function.
    private fun sendNotification(
        title: String,
        body: String?,
    ): Notification {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_directions_car_24)
            setContentTitle(title)
            setContentText(body)
            priority = NotificationManagerCompat.IMPORTANCE_HIGH
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                setCategory(Notification.CATEGORY_NAVIGATION) // shows the notification immediately
            } else {
                setCategory(Notification.CATEGORY_SERVICE)
            }
            setOngoing(true)
            setOnlyAlertOnce(true)
            setWhen(currentTimeMillis())
        }

        logger.v { "Scheduling local notification." }
        return builder.build()
    }

    private fun createPendingIntent(
        id: Int,
        intentTransform: Intent.() -> Unit = {},
    ): PendingIntent {
        val intent = IdentifiableIntent("$id", context, NotificationPublisher::class.java).apply(
            intentTransform,
        )
        return PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_TYPE_DISMISS = "DISMISS"
        const val NOTIFICATION_PAYLOAD_ID = "NOTIFICATION_PAYLOAD_ID"
        const val NOTIFICATION_PAYLOAD_TYPE = "NOTIFICATION_PAYLOAD_TYPE"
        const val NOTIFICATION_PAYLOAD_NOTIFICATION = "NOTIFICATION_PAYLOAD_NOTIFICATION"
        private const val REQUEST_CODE = 100
    }
}
