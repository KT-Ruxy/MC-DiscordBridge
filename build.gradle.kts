plugins {
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "net.elysium.plugin.discordBridge"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    implementation("net.dv8tion:JDA:5.0.0-beta.17")

    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.shadowJar {
    archiveFileName = "$archiveBaseName-$version"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}