import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "love.sola"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("love.sola.laoshipp.MainKt")
}

repositories {
    maven {
        setUrl("https://jitpack.io/")
    }
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    implementation("net.dv8tion:JDA:5.0.0-alpha.18")
    implementation("com.github.minndevelopment:jda-ktx:081a177")

    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-core:1.2.11")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    testImplementation(kotlin("test-junit5"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "17"
    }
}
