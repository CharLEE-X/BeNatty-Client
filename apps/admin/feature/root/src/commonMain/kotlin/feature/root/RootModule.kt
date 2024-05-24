package feature.root

import component.localization.localizationModule
import data.dataModule
import org.koin.dsl.module

val rootModule = module {
    includes(
        dataModule,
        localizationModule,
    )
}
