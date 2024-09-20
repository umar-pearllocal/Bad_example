// Top-level build file where you can add configuration options common to all sub-projects/modules.
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

// Make local properties available to subprojects
subprojects {
    extra["API_URL"] = localProperties["API_URL"]
    extra["API_KEY"] = localProperties["API_KEY"]
}
plugins {
    alias(libs.plugins.android.application) apply false
}

