import org.gradle.api.JavaVersion

object ProjectConfig {
    const val projectName = "benatty"
    const val packageName = "com.charleex.${projectName}"

    object Android {
        const val id = "$packageName.android"
        const val minSdk = 26
        const val compileSdk = 34
        const val targetSdk = compileSdk
        const val versionCode = 1
        const val versionName = "1.0"
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    object iOS {
        const val deploymentTarget = "15.2"
        const val summary = "Shared Module for our dating app"
        const val homepage = "Link to the Shared Module homepage"
    }

    object Kotlin {
        const val jvmTargetInt = 17
        const val jvmTarget = jvmTargetInt.toString()
        val javaVersion = JavaVersion.VERSION_17
    }
}

const val ACCOUNT = "account."
const val ADMIN = "admin."
const val AUTH = "auth."
const val CATEGORY = "category."
const val COMPONENTS = "components."
const val FEATURE = "feature."
const val PRODUCT = "product."
const val CONFIG = "config."
const val ORDER = "order."
const val SHOP = "shop."
const val TAG = "tag."
const val USER = "user."
