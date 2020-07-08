import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    application
    kotlin("kapt") version "1.3.61"
}

repositories {
    jcenter()
    maven(url = "https://oss.jfrog.org/artifactory/oss-snapshot-local/")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.0.5")
    testImplementation("io.kotest:kotest-assertions-arrow-jvm:4.0.5")

    // Arrow
    val arrowVersion = "0.10.5"
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
    implementation("io.arrow-kt:arrow-fx:$arrowVersion")
    implementation("io.arrow-kt:arrow-meta:$arrowVersion")
}

application {
    mainClassName = "io.mattmoore.kotlin.playground.AppKt"
}
