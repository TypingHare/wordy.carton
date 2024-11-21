plugins {
    kotlin("jvm") version "2.0.20"
}

group = "burrow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("/Users/jameschan/.burrow/libs/burrow-0.0.0-all.jar"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}