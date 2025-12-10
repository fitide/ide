import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version "2.2.21"
}

repositories {
    mavenCentral()
    google()
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(compose.components.resources)
            implementation("org.apache.logging.log4j:log4j-api:2.23.1")
            implementation(libs.androidx.runtime.desktop)
            implementation("org.jetbrains.jediterm:jediterm-core:3.57")
            implementation("org.jetbrains.jediterm:jediterm-ui:3.57")
            implementation("org.jetbrains.pty4j:pty4j:0.13.11")
            implementation("org.jetbrains:annotations:24.1.0")
            implementation("org.jetbrains.compose.ui:ui-graphics")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation("org.antlr:antlr4-runtime:4.13.2")
            implementation("com.googlecode.json-simple:json-simple:1.1.1")
        }
        jvmTest.dependencies {
            implementation(project.dependencies.platform("org.junit:junit-bom:5.10.0"))
            implementation("org.junit.jupiter:junit-jupiter")
        }
    }
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "org.main.ide.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "FITIDE"
            packageVersion = "1.0.0"

            macOS {
                iconFile.set(project.file("src/jvmMain/resources/icons/fitide.icns"))
            }
            windows {
                iconFile.set(project.file("src/jvmMain/resources/icons/fitide.ico"))
            }
            linux {
                iconFile.set(project.file("src/jvmMain/resources/icons/fitide.png"))
            }
        }
    }
}