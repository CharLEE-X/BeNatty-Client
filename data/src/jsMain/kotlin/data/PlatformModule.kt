package data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import io.ktor.client.engine.js.Js
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("unused")
internal actual val platformModule: Module = module {
    single<Settings>(named(SettingsType.SETTINGS_NON_ENCRYPTED.name)) {
        StorageSettings()
    }
    single<Settings>(named(SettingsType.SETTINGS_ENCRYPTED.name)) {
        StorageSettings()
    }
    factory { Js.create() }
}
