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

                with(apps.admin) {
                    implementation(feature.debug)
                    implementation(feature.root)
                    implementation(feature.router)
                    implementation(feature.dashboard)
                    implementation(feature.config)
                    implementation(feature.list)
                    implementation(feature.auth.login)
                    implementation(feature.auth.register)
                    implementation(feature.category.create)
                    implementation(feature.category.edit)
                    implementation(feature.customer.create)
                    implementation(feature.customer.edit)
                    implementation(feature.product.create)
                    implementation(feature.product.edit)
                    implementation(feature.order.page)
                    implementation(feature.tag.create)
                    implementation(feature.tag.edit)
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
