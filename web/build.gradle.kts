import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.application)
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
            ).forEach {
                head.add { link(it, "stylesheet") }
            }
        }
    }
}

kotlin {
    configAsKobwebApplication("nataliashop")

    sourceSets {
        jsMain.dependencies {
            with(projects) {
                implementation(data)
                implementation(components.core)
                implementation(components.theme)
                implementation(components.material3)
                implementation(components.localization)

                implementation(feature.debug)
                implementation(feature.root)
                implementation(feature.router)

                implementation(feature.auth.login)
                implementation(feature.auth.register)
                implementation(feature.auth.forgotPassword)
                implementation(feature.auth.updatePassword)

                implementation(feature.account.profile)
                implementation(feature.account.orders)
                implementation(feature.account.returns)
                implementation(feature.account.wishlist)

                implementation(feature.admin.dashboard)
                implementation(feature.admin.user.page)
                implementation(feature.admin.user.list)
                implementation(feature.admin.product.page)
                implementation(feature.admin.product.list)
                implementation(feature.admin.order.page)
                implementation(feature.admin.order.list)
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
            implementation(npm("@js-joda/timezone", "2.3.0"))
        }
    }
}
