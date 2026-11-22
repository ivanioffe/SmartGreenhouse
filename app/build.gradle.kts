plugins {
    alias(libs.plugins.ioffeivan.android.application)
    alias(libs.plugins.ioffeivan.compose)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.hilt)
    alias(libs.plugins.ioffeivan.ktlint)
}

android {
    namespace = "com.ioffeivan.smartgreenhouse"

    defaultConfig {
        applicationId = "com.ioffeivan.smartgreenhouse"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.resources)

    implementation(projects.feature.appSettings)
    implementation(projects.feature.onboarding)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)
}
