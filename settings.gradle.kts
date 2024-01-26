rootProject.name = "natalia-shop"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://jitpack.io")
        mavenLocal()
    }
}

includeBuild("convention-plugins")
include(
    ":web",
    ":composeApp",

    ":data",

    ":components:core",
    ":components:location",
    ":components:notification",
    ":components:pictures",
    ":components:localization",
    ":components:theme",
    ":components:material3",

    ":feature:debug",
    ":feature:root",
    ":feature:router",
    ":feature:auth:login",
    ":feature:auth:register",
    ":feature:auth:forgot-password",
    ":feature:auth:update-password",
    ":feature:account:profile",
    ":feature:account:orders",
    ":feature:account:returns",
    ":feature:account:wishlist",
    ":feature:admin:dashboard",
    ":feature:admin:users",
    ":feature:admin:products",
    ":feature:admin:orders",
)
