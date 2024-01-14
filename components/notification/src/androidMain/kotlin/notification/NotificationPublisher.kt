package notification

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.core.component.KoinComponent

internal class NotificationPublisher : BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationsManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId =
            intent.getIntExtra(NotificationServiceAndroid.NOTIFICATION_PAYLOAD_ID, 0)
        val notificationType =
            intent.getStringExtra(NotificationServiceAndroid.NOTIFICATION_PAYLOAD_TYPE)

        @Suppress("DEPRECATION")
        val notification =
            intent.getParcelableExtra<Notification>(
                NotificationServiceAndroid.NOTIFICATION_PAYLOAD_NOTIFICATION
            )

        if (notificationType == NotificationServiceAndroid.NOTIFICATION_TYPE_DISMISS) {
            notificationsManager.cancel(notificationId)
        } else {
            notificationsManager.notify(notificationId, notification)
        }
    }
}
