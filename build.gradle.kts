import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    base
    jacoco
    kotlin("jvm") version "1.7.20" apply false

    // Printing unit tests in mocha-style
    id("com.adarshr.test-logger") version "3.2.0"
    // Cyclomatic complexity calculator
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
    // Checking for newer versions
    id("com.github.ben-manes.versions").version("0.42.0")
    // ktlint plugin
    id("org.jlleitschuh.gradle.ktlint").version("11.0.0")
}

// Declaring variables to be populated from gradle.properties
val arrowVersion: String by project
val kotestVersion: String by project
val kotestAssertionsVersion: String by project
val kotestAssertionsArrowVersion: String by project
val kotestExtensionsVersion: String by project
val kotlinLoggingVersion: String by project
val ktlintVersion: String by project
val logbackVersion: String by project
val jacocoVersion: String by project
val testcontainersVersion: String by project

allprojects {
    group = "kotlinplayground"

    repositories {
        mavenCentral()
    }

    apply(plugin = "java")
    apply(plugin = "com.adarshr.test-logger")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

    ktlint {
        version.set(ktlintVersion)
        enableExperimentalRules.set(true)
        filter {
            // exclude any built files
            exclude({ it.file.absolutePath.contains("/generated/") })
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")

        // Arrow
        implementation("io.arrow-kt:arrow-core:$arrowVersion")
        implementation("io.arrow-kt:arrow-fx-coroutines:$arrowVersion")
        implementation("io.arrow-kt:arrow-fx-stm:$arrowVersion")

        // logging
        implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
        implementation("ch.qos.logback:logback-classic:$logbackVersion")

        // kotest
        testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestAssertionsVersion")
        testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-arrow:$kotestAssertionsArrowVersion")
        testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:$kotestExtensionsVersion")

        // testcontainers
        testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
        testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()

            testlogger {
                setTheme("mocha")
            }
        }

        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    detekt {
        buildUponDefaultConfig = true
        config = files("../resources/detekt-config.yml")
    }

    jacoco {
        toolVersion = jacocoVersion
    }
}

// When running `dependencyUpdates`, only suggest stable releases
// See https://github.com/ben-manes/gradle-versions-plugin/blob/master/examples/kotlin/build.gradle.kts
fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

// When running `dependencyUpdates`, only suggest stable releases
// See https://github.com/ben-manes/gradle-versions-plugin/blob/master/examples/kotlin/build.gradle.kts
tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.isNonStable()
    }
}

// Jacoco Test Coverage Report
task<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.withType<Test>() })
    dependsOn(subprojects.map { it.tasks.withType<JacocoReport>() })
    sourceDirectories.setFrom(subprojects.map { it.the<SourceSetContainer>()["main"].allSource.srcDirs })
    classDirectories.setFrom(subprojects.map(::getClassDirectories))
    executionData.setFrom(project.fileTree(".") { include("**/build/jacoco/test.exec") })
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("$buildDir/reports/jacoco/report.xml"))
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(file("$buildDir/reports/jacoco/html"))
    }
}

fun getClassDirectories(project: Project): FileTree {
    return files(project.the<SourceSetContainer>()["main"].output).asFileTree.matching {
        exclude("**/com/kotlinplayground/**/*.class") // Excluding gRPC generated files
    }
}

tasks {
    jacocoTestCoverageVerification {
        sourceDirectories.setFrom(subprojects.map { it.the<SourceSetContainer>()["main"].allSource.srcDirs })
        classDirectories.setFrom(subprojects.map(::getClassDirectories))
        executionData.setFrom(project.fileTree(".") { include("**/build/jacoco/test.exec") })
        violationRules {
            rule { limit { minimum = BigDecimal.valueOf(0.10) } }
        }
    }
    check {
        dependsOn(jacocoTestCoverageVerification)
        dependsOn(":jacocoRootReport")
    }
}
