package component.localization

import org.koin.dsl.module

val localizationModule = module {
    single<InputValidator> { InputValidatorImpl() }
}
