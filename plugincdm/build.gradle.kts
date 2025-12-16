plugins {
    id("java")
    id("antlr")
    idea
}

group = "org.cdm"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.antlr:antlr4-runtime:4.13.2")
    compileOnly(project(":FITIDE"))
    antlr("org.antlr:antlr4:4.13.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    archiveBaseName.set("plugincdm")
    from(sourceSets.main.get().output)
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = listOf("-visitor", "-listener")
}

tasks.compileJava {
    dependsOn(tasks.generateGrammarSource)
}

//Я хотел сделать обязательную генерацию парсера и его подгрузку, но у меня ничего не зафурычило
//Так что это пока просто место, откуда можно перетаскать классы
sourceSets.main {
    java.srcDir("build/generated-src/antlr/main")
}
