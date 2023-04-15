plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
    alias(libs.plugins.jib)
}

group = "love.sola"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

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
    implementation(libs.kotlin.serialization)

    implementation(libs.slf4k)
    implementation(libs.bundles.logback)

    implementation(libs.jda)
    implementation(libs.jda.ktx)

    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jib {
    from {
        image = "eclipse-temurin:17"
    }
    to {
        image = "unlimitedsola/laoshi-plus-plus"
    }
    container {
        appRoot = "/app"
        workingDirectory = appRoot
    }
}
