import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.codegen.annotations)
            implementation(libs.kotlin.inject.runtime)
            implementation(libs.kotlin.inject.anvil.runtime)
            implementation(libs.kotlin.inject.anvil.runtime.optional)
            implementation(libs.ktor.client)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor3)
            implementation(libs.kermit)
        }
    }
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
    arg("circuit.codegen.lenient", "true")
    arg("circuit.codegen.mode", "kotlin_inject_anvil")
    arg(
        "kotlin-inject-anvil-contributing-annotations",
        "com.slack.circuit.codegen.annotations.CircuitInject"
    )
}

dependencies {
    // https://developer.android.com/studio/write/java8-support#library-desugaring
    //coreLibraryDesugaring(libs.desugar)

    ksp(libs.kotlin.inject.compiler)
    ksp(libs.kotlin.inject.anvil.compiler)
    //api(libs.circuit.codegen.annotations)
    ksp(libs.circuit.codegen)

    //debugImplementation(compose.uiTooling)
}
