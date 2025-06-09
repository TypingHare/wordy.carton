group = "burrow"
version = "0.0.0"

plugins { kotlin("jvm") version "2.0.20" }
repositories { mavenCentral() }
kotlin { jvmToolchain(21) }

dependencies {
    val homeDirectory = System.getProperty("user.home") as String
    val burrowLibRootPath = File(homeDirectory, ".local/share/burrow/lib")
    val burrowLibs = burrowLibRootPath
        .listFiles { file -> file.extension == "jar" }
        ?.toList() ?: emptyList()
    implementation(files(burrowLibs))

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

tasks.jar {
    archiveBaseName.set("wordy.carton")
    archiveVersion.set("")
}
