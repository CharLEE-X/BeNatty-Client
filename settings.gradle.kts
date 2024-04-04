rootProject.name = "BeNatty-Client"
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
//    ":composeApp",

    ":data",
    ":core",

    ":components:location",
    ":components:notification",
    ":components:pictures",
    ":components:localization",
    ":components:theme",
    ":components:material3",

    // Shared features
    ":feature:debug",
    ":feature:root",
    ":feature:router",

    ":feature:auth:login",
    ":feature:auth:register",
    ":feature:auth:forgot-password",
    ":feature:auth:update-password",

    // Shop features
    ":feature:shop:navbar",
    ":feature:shop:footer",
    ":feature:shop:home",
    ":feature:shop:account:profile",
    ":feature:shop:account:orders",
    ":feature:shop:account:returns",
    ":feature:shop:account:wishlist",
    ":feature:shop:product:catalog",
    ":feature:shop:product:page",

    // Admin features
    ":feature:admin:dashboard",
    ":feature:admin:config",
    ":feature:admin:list",
    ":feature:admin:category:create",
    ":feature:admin:category:edit",
    ":feature:admin:customer:create",
    ":feature:admin:customer:edit",
//    ":feature:admin:product:create",
    ":feature:admin:product:page",
    ":feature:admin:order:create",
    ":feature:admin:order:page",
    ":feature:admin:tag:create",
    ":feature:admin:tag:edit",
)
