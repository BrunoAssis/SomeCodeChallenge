import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
    application
    jacoco
    // For Gradle test to have a useful output :P
    id("com.adarshr.test-logger") version "1.7.0"
}

group = "com.brunoassis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.javalin:javalin:3.3.0")
    implementation("org.slf4j:slf4j-simple:1.7.26")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.9")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.9")

    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("io.mockk:mockk:1.9.3")

    // For HTTP calls used in Integration tests
    testImplementation("com.github.jkcclemens:khttp:-SNAPSHOT")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClassName = "com.brunoassis.corachallenge.application.MainKt"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.destination = file("$buildDir/jacocoHtml")
    }
}