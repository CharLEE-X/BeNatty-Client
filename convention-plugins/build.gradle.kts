plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.androidx.gradle)
    implementation(libs.detekt)
    implementation(libs.kotlin.gradle)
    implementation(libs.kover)
    implementation(libs.ksp)
    implementation(libs.ktLint)
    implementation(libs.spotless)
}
