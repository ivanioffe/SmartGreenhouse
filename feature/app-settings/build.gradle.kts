plugins {
    alias(libs.plugins.ioffeivan.android.library)
    alias(libs.plugins.ioffeivan.compose)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.hilt)
    alias(libs.plugins.ioffeivan.ktlint)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ioffeivan.feature.app_settings"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastoreAppSettings)
    implementation(projects.core.datastoreSession)
    implementation(projects.core.mvu)
    implementation(projects.core.resources)
    implementation(projects.core.ui)

    api(projects.core.model)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    api(libs.kotlinx.coroutines.core)
}
