import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.application)
    kotlin("plugin.serialization")
}

group = project.name
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")

            listOf(
                "https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined",
                "https://fonts.googleapis.com/css?family=Roboto",
                "https://fonts.googleapis.com/css?family=Montserrat",
                "https://fonts.googleapis.com/css?family=DM+Sans",
            ).forEach {
                head.add { link(it, "stylesheet") }
            }
        }
    }
}

kotlin {
    configAsKobwebApplication("benatty")

    sourceSets {
        jsMain.dependencies {
            with(projects) {
                implementation(data)
                implementation(core)
//                implementation(components.theme)
                implementation(components.localization)

                with(apps.shop.feature) {
                    implementation(root)
                    implementation(router)
                    implementation(auth.login)
                    implementation(auth.register)
                    implementation(auth.forgotPassword)
                    implementation(auth.updatePassword)
                    implementation(navbar)
                    implementation(footer)
                    implementation(cart)
                    implementation(home)
                    implementation(account.profile)
                    implementation(account.orders)
                    implementation(account.returns)
                    implementation(account.wishlist)
                    implementation(product.catalog)
                    implementation(product.page)
                    implementation(checkout)
                }
            }

            implementation(compose.runtime)
            implementation(compose.html.core)
            implementation(compose.html.svg)

            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
            implementation(libs.silk.icons.mdi)

            implementation(libs.ballast.core)
            implementation(libs.ballast.navigation)

            implementation(libs.koin.core)
            implementation(libs.kotlin.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(npm("@js-joda/timezone", "2.3.0"))
        }
    }
}
