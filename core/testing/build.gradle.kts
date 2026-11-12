plugins {
    alias(libs.plugins.ioffeivan.jvm.library)
    alias(libs.plugins.ioffeivan.detekt)
    alias(libs.plugins.ioffeivan.ktlint)
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(platform(libs.test.junit5.bom))
    api(libs.test.junit5.api)
}
