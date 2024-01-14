package feature.root

import data.dataModule
import org.koin.dsl.module

val rootModule = module {
    includes(dataModule)
}
