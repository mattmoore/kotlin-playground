plugins {
  id("org.jetbrains.kotlin.jvm")
  `java-library`
  kotlin("kapt")
  `maven-publish`
}

group = "io.mattmoore.kotlin.annotation"
version = "0.1.0-SNAPSHOT"

repositories {
  jcenter()
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("com.google.auto.service:auto-service:1.0-rc4")
  kapt("com.google.auto.service:auto-service:1.0-rc4")
  implementation("com.squareup:kotlinpoet:1.6.0")
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

java {
  withSourcesJar()
  withJavadocJar()
}

publishing {
  publications {
    create<MavenPublication>("annotation-processor") {
      from(components["java"])
      groupId = "io.mattmoore.kotlin.annotation"
      artifactId = "annotation-processor"
      version = version
    }
  }
}
