plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Ktor
    val ktorVersion = "1.3.2"
    implementation("io.ktor:ktor-server-core:${ktorVersion}")
    implementation("io.ktor:ktor-server-netty:${ktorVersion}")
    implementation("io.ktor:ktor-gson:${ktorVersion}")
    implementation("io.ktor:ktor-auth-jwt:${ktorVersion}")

    // Arrow
    val arrowVersion = "0.10.5"
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
    implementation("io.arrow-kt:arrow-fx:$arrowVersion")
    implementation("io.arrow-kt:arrow-meta:$arrowVersion")
}

application {
    mainClassName = "io.mattmoore.ktor.playground.server.ServerKt"
}
