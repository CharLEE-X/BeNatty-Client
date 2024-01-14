package location

import co.touchlab.kermit.Logger.Companion.withTag
import org.koin.core.module.Module
import org.koin.dsl.module

actual val locationModule: Module = module {
    single<LocationService>(createdAtStart = true) {
        LocationServiceIos(
            logger = withTag(LocationService::class.simpleName!!),
        )
    }
}
