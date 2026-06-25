plugins {
    id("java-library")
}

group = "org.ide.PluginController.PluginInterface"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    api("org.antlr:antlr4-runtime:4.13.2")
}

tasks.test {
    useJUnitPlatform()
}

