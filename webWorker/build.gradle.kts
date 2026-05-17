import org.gradle.kotlin.dsl.project

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.protobuf") version "0.9.4"
}

group = "org.webWorker"
version = "unspecified"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // gRPC Server
    implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
    implementation("io.grpc:grpc-stub:1.74.0")
    implementation("io.grpc:grpc-protobuf:1.74.0")
    implementation("io.grpc:grpc-netty-shaded:1.74.0")
    implementation("com.google.protobuf:protobuf-java:4.28.2")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.0")

    // Для аннотаций @Generated
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // Для тестирования
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.28.2"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.74.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach { task ->
            task.plugins {
                create("grpc") {
                    option("jakarta_omit")  // Опускает javax аннотации
                }
            }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// Создаем fat JAR для запуска
tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.example.grpc.GrpcServerApplication"
    }
}

tasks.bootJar {
    archiveFileName.set("grpc-server.jar")
    mainClass.set("com.example.grpc.GrpcServerApplication")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}