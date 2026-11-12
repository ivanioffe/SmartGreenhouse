import com.android.build.api.dsl.CommonExtension
import com.ioffeivan.build_logic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class LocalTestConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }

            dependencies {
                "testImplementation"(platform(libs.findLibrary("test-junit5-bom").get()))
                "testImplementation"(libs.findLibrary("test-junit5-api").get())
                "testImplementation"(libs.findLibrary("test-junit5-params").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("mockk").get())
                "testImplementation"(libs.findLibrary("truth").get())
                "testImplementation"(libs.findLibrary("turbine").get())

                "testRuntimeOnly"(libs.findLibrary("test-junit-platform-launcher").get())
                "testRuntimeOnly"(libs.findLibrary("test-junit5-engine").get())
                "testRuntimeOnly"(libs.findLibrary("test-junit-vintage-engine").get())
            }

            pluginManager.withPlugin("ioffeivan.compose") {
                extensions.configure<CommonExtension> {
                    testOptions.unitTests.isIncludeAndroidResources = true
                }

                dependencies {
                    "testImplementation"(platform(libs.findLibrary("androidx-compose-bom").get()))
                    "testImplementation"(libs.findLibrary("androidx-compose-ui-test-junit4").get())
                    "testImplementation"(libs.findLibrary("robolectric").get())
                }
            }
        }
    }
}
