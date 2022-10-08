import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.protobuf") version("0.9.0")
    kotlin("jvm")
    application

    // Shadow JAR
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

project.setProperty("mainClassName", "grpc.AppKt")

// Declaring variables to be populated from gradle.properties
val grpcNetty: String by project
val grpcProtobuf: String by project
val grpcStub: String by project
val grpcKotlinStub: String by project
val protobufKotlin: String by project
val protobufJava: String by project

dependencies {
    implementation(project(":common"))

    // Grpc and Protobuf
    implementation("io.grpc:grpc-netty:$grpcNetty")
    implementation("io.grpc:grpc-protobuf:$grpcProtobuf")
    implementation("io.grpc:grpc-stub:$grpcStub")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinStub")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufKotlin")
    implementation("com.google.protobuf:protobuf-java:$protobufJava")
    implementation(kotlin("stdlib-jdk8"))
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/grpckt")
            srcDirs("build/generated/source/proto/main/java")
        }
        proto {
            srcDir("../proto")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufJava"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcProtobuf"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinStub:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}

repositories {
    // jitpack releases are required until we start publishing to maven
    maven(url = "https://jitpack.io")
    mavenCentral()
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("grpcserver")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass))
        }
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}