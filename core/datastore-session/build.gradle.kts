plugins {
    alias(libs.plugins.ioffeivan.datastore)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.ktlint)
}

android {
    namespace = "com.ioffeivan.core.datastore_session"
}

dependencies {
    implementation(projects.core.common)

    api(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.datastore.tink)
}
