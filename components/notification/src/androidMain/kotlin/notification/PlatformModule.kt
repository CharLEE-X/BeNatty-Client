package notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import co.touchlab.kermit.Logger.Companion.withTag
import org.koin.core.module.Module
import org.koin.dsl.module

actual val notificationModule: Module = module {
    single {
        get<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
    single {
        get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    single<NotificationService> {
        NotificationServiceAndroid(
            logger = withTag(NotificationService::class.java.simpleName),
            context = get(),
            activity = inject(),
            notificationManager = get(),
            alarmManager = get(),
        )
    }
}
