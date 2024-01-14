package notification

import co.touchlab.kermit.Logger.Companion.withTag
import org.koin.core.module.Module
import org.koin.dsl.module

actual val notificationModule: Module = module {
    single<NotificationService>(createdAtStart = true) {
        NotificationServiceJs(
            logger = withTag(NotificationService::class.simpleName!!),
        )
    }
}
