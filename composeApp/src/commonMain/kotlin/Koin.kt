import feature.debug.debugModule
import feature.root.rootModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module

internal fun initKoin(additionalModules: List<Module> = emptyList()) =
    startKoin {
        modules(listOf(rootModule, debugModule) + additionalModules)
        createEagerInstances()
    }
