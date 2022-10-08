plugins {
    kotlin("jvm")
}

val logbackVersion: String by project
val kotlinFakerVersion: String by project
val jdbiVersion: String by project
val postgresqlVersion: String by project
val hikaricpVersion: String by project

repositories {
    maven(url = "https://dl.bintray.com/serpro69/maven/")
}

dependencies {
    implementation(project(":common"))
    // JDBI
    implementation("org.jdbi:jdbi3-core:$jdbiVersion")
    implementation("org.jdbi:jdbi3-sqlobject:$jdbiVersion")
    implementation("org.jdbi:jdbi3-kotlin:$jdbiVersion")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:$jdbiVersion")
    implementation("org.jdbi:jdbi3-postgres:$jdbiVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("com.zaxxer:HikariCP:$hikaricpVersion")

    // Faker for fake data
    testImplementation("io.github.serpro69:kotlin-faker:$kotlinFakerVersion")
}
