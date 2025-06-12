import java.io.FileInputStream
import java.util.Properties

group = "burrow"
version = "0.0.0"

plugins { kotlin("jvm") version "2.0.20" }
repositories { mavenCentral() }
kotlin { jvmToolchain(21) }

dependencies {
    val homeDirectory = System.getProperty("user.home") as String
    val burrowLibRootPath = File(homeDirectory, ".local/share/burrow/lib")
    val properties = Properties()
    val propsFile = File("src/main/resources/burrow.properties")
    if (propsFile.exists()) {
        FileInputStream(propsFile).use { properties.load(it) }
    }
    val excludedJars = properties.getProperty("burrow.carton.jars")
        ?.split(":")
        ?.map { it.trim() }
        ?.toSet() ?: emptySet()
    val burrowLibs = burrowLibRootPath
        .listFiles { file -> file.extension == "jar" && file.name !in excludedJars }
        ?.toList() ?: emptyList()
    implementation(files(burrowLibs))

    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

tasks.jar {
    archiveBaseName.set("wordy.carton")
    archiveVersion.set("")
}
