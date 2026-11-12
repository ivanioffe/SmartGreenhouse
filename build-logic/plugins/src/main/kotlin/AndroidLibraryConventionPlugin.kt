import com.android.build.api.dsl.LibraryExtension
import com.ioffeivan.build_logic.catalogVersion
import com.ioffeivan.build_logic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("com.android.library")

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.targetSdk = catalogVersion("targetSdk").toInt()
                lint.targetSdk = catalogVersion("targetSdk").toInt()
            }
        }
    }
}
