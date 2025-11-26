import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
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
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "FITIDE"
            packageVersion = "1.0.0"
        }
    }
}
