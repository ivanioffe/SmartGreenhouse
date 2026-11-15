plugins {
    alias(libs.plugins.ioffeivan.android.library)
    alias(libs.plugins.ioffeivan.compose)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.ktlint)
}

android {
    namespace = "com.ioffeivan.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    implementation(projects.core.resources)

    implementation(libs.androidx.activity.compose)
}
