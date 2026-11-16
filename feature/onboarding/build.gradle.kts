plugins {
    alias(libs.plugins.ioffeivan.android.library)
    alias(libs.plugins.ioffeivan.compose)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.ktlint)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ioffeivan.feature.onboarding"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.resources)
    implementation(projects.core.ui)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
}
