package com.charleex.benatty

import android.app.Application
import android.content.Context
import initKoin
import org.koin.dsl.module

class ShopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            additionalModules = listOf(
                module {
                    single<Context> { this@ShopApplication }
                },
            ),
        )
    }
}
