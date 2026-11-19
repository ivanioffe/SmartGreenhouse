plugins {
    alias(libs.plugins.ioffeivan.datastore)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.ktlint)
}

android {
    namespace = "com.ioffeivan.core.datastore_app_settings"
}

dependencies {
    implementation(projects.core.common)

    api(projects.core.model)
    api(libs.kotlinx.coroutines.core)
}
