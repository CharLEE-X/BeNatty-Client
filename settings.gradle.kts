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
    ":data",
    ":core",

    ":components:location",
    ":components:notification",
    ":components:pictures",
    ":components:localization",
//    ":components:theme",
    ":components:swiper",

    ":apps:shop:web",
//    ":apps:shop:composeApp",
    ":apps:shop:feature:root",
    ":apps:shop:feature:router",
    ":apps:shop:feature:auth:login",
    ":apps:shop:feature:auth:register",
    ":apps:shop:feature:auth:forgot-password",
    ":apps:shop:feature:auth:update-password",
    ":apps:shop:feature:home",
    ":apps:shop:feature:navbar",
    ":apps:shop:feature:footer",
    ":apps:shop:feature:cart",
    ":apps:shop:feature:account:profile",
    ":apps:shop:feature:account:orders",
    ":apps:shop:feature:account:returns",
    ":apps:shop:feature:account:wishlist",
    ":apps:shop:feature:product:catalog",
    ":apps:shop:feature:product:page",
    ":apps:shop:feature:checkout",

    ":apps:admin:web",
    ":apps:admin:feature:root",
    ":apps:admin:feature:router",
    ":apps:admin:feature:debug",
    ":apps:admin:feature:dashboard",
    ":apps:admin:feature:config",
    ":apps:admin:feature:list",
    ":apps:admin:feature:auth:login",
    ":apps:admin:feature:auth:register",
    ":apps:admin:feature:category:create",
    ":apps:admin:feature:category:edit",
    ":apps:admin:feature:customer:create",
    ":apps:admin:feature:customer:edit",
    ":apps:admin:feature:product:create",
    ":apps:admin:feature:product:edit",
    ":apps:admin:feature:order:create",
    ":apps:admin:feature:order:page",
    ":apps:admin:feature:tag:create",
    ":apps:admin:feature:tag:edit",
)
