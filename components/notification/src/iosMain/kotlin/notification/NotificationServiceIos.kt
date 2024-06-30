package notification

import co.touchlab.kermit.Logger
import com.benasher44.uuid.uuid4
import core.models.PermissionStatus
import core.util.mainContinuation
import kotlinx.datetime.Clock
import kotlinx.datetime.toNSDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitTimeZone
import platform.Foundation.NSCalendarUnitYear
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatus
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationInterruptionLevel
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSettings
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNNotificationTrigger
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject
import kotlin.coroutines.suspendCoroutine

internal class NotificationServiceIos(private val logger: Logger) : NotificationService {
    private val delegate =
        object : UNUserNotificationCenterDelegateProtocol, NSObject() {
            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                willPresentNotification: UNNotification,
                withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
            ) {
                withCompletionHandler(UNNotificationPresentationOptionAlert)
            }
        }

    private val notificationCenter: UNUserNotificationCenter by lazy {
        UNUserNotificationCenter.currentNotificationCenter().apply {
            delegate = this@NotificationServiceIos.delegate
        }
    }

    override suspend fun checkPermission(): PermissionStatus {
        val status =
            suspendCoroutine { continuation ->
                notificationCenter.getNotificationSettingsWithCompletionHandler(
                    mainContinuation { settings: UNNotificationSettings? ->
                        continuation.resumeWith(
                            Result.success(
                                settings?.authorizationStatus ?: UNAuthorizationStatusNotDetermined,
                            ),
                        )
                    },
                )
            }
        return when (status) {
            UNAuthorizationStatusAuthorized,
            UNAuthorizationStatusProvisional,
            UNAuthorizationStatusEphemeral,
            -> PermissionStatus.GRANTED

            UNAuthorizationStatusNotDetermined -> PermissionStatus.NOT_DETERMINED
            UNAuthorizationStatusDenied -> PermissionStatus.DENIED
            else -> error("unknown push authorization status $status")
        }
    }

    override suspend fun requestPermission() {
        val status: UNAuthorizationStatus =
            suspendCoroutine { continuation ->
                notificationCenter.getNotificationSettingsWithCompletionHandler(
                    mainContinuation { settings: UNNotificationSettings? ->
                        continuation.resumeWith(
                            Result.success(settings?.authorizationStatus ?: UNAuthorizationStatusNotDetermined),
                        )
                    },
                )
            }
        when (status) {
            UNAuthorizationStatusAuthorized -> return
            UNAuthorizationStatusNotDetermined -> {
                val isSuccess =
                    suspendCoroutine { continuation ->
                        UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
                            UNAuthorizationOptionSound
                                .or(UNAuthorizationOptionAlert)
                                .or(UNAuthorizationOptionBadge),
                            mainContinuation { isOk, error ->
                                if (isOk && error == null) {
                                    continuation.resumeWith(Result.success(true))
                                } else {
                                    continuation.resumeWith(Result.success(false))
                                }
                            },
                        )
                    }
                if (isSuccess) {
                    requestPermission()
                } else {
                    error("Notifications permission failed")
                }
            }

            UNAuthorizationStatusDenied -> error("Notifications permission denied")
            else -> error("Notifications permission status $status")
        }
    }

    override fun openSettingPage() {
        openSettingPage()
    }

    override suspend fun schedule(notificationType: NotificationType) {
        logger.v { "Scheduling notification: $notificationType" }
        val timeNow = Clock.System.now()
        when (notificationType) {
            is NotificationType.Immediate -> {
                val trigger =
                    UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                        0.1,
                        false,
                    )
                sendNotification(
                    title = notificationType.title,
                    body = notificationType.body,
                    soundType = notificationType.soundType,
                    trigger = trigger,
                )
            }

            is NotificationType.Scheduled -> {
                val allUnits =
                    NSCalendarUnitSecond or
                        NSCalendarUnitMinute or
                        NSCalendarUnitHour or
                        NSCalendarUnitDay or
                        NSCalendarUnitMonth or
                        NSCalendarUnitYear or
                        NSCalendarUnitTimeZone
                val deliveryDate = notificationType.delivery.toNSDate()
                val dateComponents = NSCalendar.currentCalendar.components(allUnits, deliveryDate)
                val trigger =
                    UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
                        dateComponents = dateComponents,
                        repeats = false,
                    )
                sendNotification(
                    title = notificationType.title,
                    body = notificationType.body,
                    soundType = notificationType.soundType,
                    trigger = trigger,
                )
            }
        }
    }

    override suspend fun cancelNotification(ids: List<String>) {
        ids.ifEmpty { return }
        logger.v { "Cancelling pending notifications with IDs: [${ids.joinToString()}]" }
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(ids)
        notificationCenter.removeDeliveredNotificationsWithIdentifiers(ids)
    }

    private fun sendNotification(
        title: String,
        body: String?,
        soundType: SoundType,
        trigger: UNNotificationTrigger,
    ) {
        val content =
            UNMutableNotificationContent().apply {
                setTitle(title)
                body?.let { setBody(it) }
                soundType.toUNNotificationSound()?.let { setSound(it) }
                setInterruptionLevel(UNNotificationInterruptionLevel.UNNotificationInterruptionLevelTimeSensitive)
            }
        val request =
            UNNotificationRequest.requestWithIdentifier(
                identifier = uuid4().toString(),
                content = content,
                trigger = trigger,
            )

        notificationCenter.addNotificationRequest(request) { error ->
            if (error == null) {
                logger.v { "Successfully scheduled notification" }
            } else {
                logger.e { "Failed to schedule notification. Error: $error" }
            }
        }

        notificationCenter.getDeliveredNotificationsWithCompletionHandler { notifications ->
            notifications?.forEach { notification ->
                logger.v { "Delivered notification: $notification" }
            }
        }

        notificationCenter.getPendingNotificationRequestsWithCompletionHandler { notifications ->
            notifications?.forEach { notification ->
                logger.v { "Pending notification: $notification" }
            }
        }
    }
}

private fun SoundType.toUNNotificationSound(): UNNotificationSound? {
    return when (this) {
        SoundType.NONE -> null
        SoundType.DEFAULT -> UNNotificationSound.defaultSound()
        SoundType.CRITICAL -> UNNotificationSound.defaultCriticalSound()
    }
}
