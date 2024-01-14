package component.pictures

import co.touchlab.kermit.Logger.Companion.withTag
import org.koin.dsl.module

@Suppress("unused")
actual val picturesModule = module {
    single<PicturesService> {
        PicturesServiceIos(
            logger = withTag(PicturesService::class.simpleName!!),
        )
    }
}
