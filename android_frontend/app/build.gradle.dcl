androidApplication {
    namespace = "org.example.app"

    dependencies {
        // Compose BOM to align versions across artifacts
        implementation(platform("androidx.compose:compose-bom:2024.09.01"))

        // Core Compose runtime (explicit versions to align with UI/Test artifacts)
        implementation("androidx.compose.runtime:runtime:1.7.4")
        implementation("androidx.compose.runtime:runtime-livedata:1.7.4")

        // UI
        implementation("androidx.activity:activity-compose:1.9.3")
        implementation("androidx.compose.ui:ui:1.7.4")
        implementation("androidx.compose.ui:ui-tooling-preview:1.7.4")

        // Material 3 and icons (explicit versions required by Declarative Gradle)
        implementation("androidx.compose.material3:material3:1.3.0")
        implementation("androidx.compose.material3:material3-window-size-class:1.3.0")
        implementation("androidx.compose.material:material-icons-extended:1.7.4")
        implementation("androidx.compose.foundation:foundation:1.7.4")
        implementation("androidx.compose.foundation:foundation-layout:1.7.4")

        // Lifecycle
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

        // Unit test dependencies
        implementation("junit:junit:4.13.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
        implementation("io.mockk:mockk:1.13.13")
        implementation("androidx.arch.core:core-testing:2.2.0")

        // Android instrumented and Compose UI test dependencies
        implementation("androidx.test.ext:junit:1.2.1")
        implementation("androidx.test:core:1.6.1")
        implementation("androidx.test:runner:1.6.2")
        implementation("androidx.test:rules:1.6.1")
        implementation("androidx.compose.ui:ui-test-junit4:1.7.4")
        implementation("androidx.compose.ui:ui-test-manifest:1.7.4")
    }
}
