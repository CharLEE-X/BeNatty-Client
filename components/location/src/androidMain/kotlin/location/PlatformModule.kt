package location

import co.touchlab.kermit.Logger.Companion.withTag
import org.koin.core.module.Module
import org.koin.dsl.module

actual val locationModule: Module =
    module {
        single<LocationService> {
            LocationServiceAndroid(
                logger = withTag(LocationService::class.java.simpleName),
                context = get(),
                activity = inject(),
            )
        }
    }
