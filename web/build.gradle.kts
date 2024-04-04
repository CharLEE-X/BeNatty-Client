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
    configAsKobwebApplication("nataliashop")

    sourceSets {
        jsMain.dependencies {
            with(projects) {
                implementation(data)
                implementation(core)
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

                implementation(feature.shop.navbar)
                implementation(feature.shop.footer)
                implementation(feature.shop.home)
                implementation(feature.shop.account.profile)
                implementation(feature.shop.account.orders)
                implementation(feature.shop.account.returns)
                implementation(feature.shop.account.wishlist)
                implementation(feature.shop.product.catalog)
                implementation(feature.shop.product.page)

                implementation(feature.admin.dashboard)
                implementation(feature.admin.config)
                implementation(feature.admin.list)
                implementation(feature.admin.category.create)
                implementation(feature.admin.category.edit)
                implementation(feature.admin.customer.create)
                implementation(feature.admin.customer.edit)
                implementation(feature.admin.product.page)
                implementation(feature.admin.order.page)
                implementation(feature.admin.tag.page)

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
