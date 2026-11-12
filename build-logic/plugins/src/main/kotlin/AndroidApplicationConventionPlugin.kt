import com.android.build.api.dsl.ApplicationExtension
import com.ioffeivan.build_logic.catalogVersion
import com.ioffeivan.build_logic.configureKotlinAndroid
import com.ioffeivan.build_logic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("com.android.application")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = catalogVersion("targetSdk").toInt()
            }

            dependencies {
                "implementation"(libs.findLibrary("androidx-activity-compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
            }
        }
    }
}
