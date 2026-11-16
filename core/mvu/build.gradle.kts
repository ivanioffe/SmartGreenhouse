plugins {
    alias(libs.plugins.ioffeivan.android.library)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.ktlint)
}

android {
    namespace = "com.ioffeivan.core.mvu"

    testFixtures {
        enable = true
    }
}

dependencies {
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    testFixturesImplementation(libs.truth)
}
