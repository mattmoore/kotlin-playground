plugins {
    id("org.jetbrains.kotlin.jvm")
    kotlin("kapt")
    application
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    implementation(project(":plugins:annotation-processor:compiler-plugin"))
    kapt(project(":plugins:annotation-processor:compiler-plugin"))
}

application {
    mainClassName = "io.mattmoore.kotlin.plugins.annotations.example.AppKt"
}
