androidApplication {
    namespace = "org.example.app"

    dependencies {
        // Compose BOM to align versions across artifacts
        implementation(platform("androidx.compose:compose-bom:2024.09.01"))

        // Core Compose runtime
        implementation("androidx.compose.runtime:runtime")
        implementation("androidx.compose.runtime:runtime-livedata")

        // UI
        implementation("androidx.activity:activity-compose:1.9.3")
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-tooling-preview")

        // Material 3 and icons
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.material:material-icons-extended")

        // Lifecycle
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    }
}
