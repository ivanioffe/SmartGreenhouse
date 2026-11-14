plugins {
    alias(libs.plugins.ioffeivan.android.library)
    alias(libs.plugins.ioffeivan.compose)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.ktlint)
}

android {
    namespace = "com.ioffeivan.core.designsystem"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.resources)
}
