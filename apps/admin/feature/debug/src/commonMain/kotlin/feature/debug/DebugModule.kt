package feature.debug

import component.pictures.picturesModule
import location.locationModule
import notification.notificationModule
import org.koin.dsl.module

val debugModule =
    module {
        includes(
            locationModule,
            notificationModule,
            picturesModule,
        )
    }
