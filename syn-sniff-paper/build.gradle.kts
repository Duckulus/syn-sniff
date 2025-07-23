plugins {
    `java-library`
    id("com.gradleup.shadow") version "8.3.8"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("de.eldoria.plugin-yml.bukkit") version "0.7.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":${rootProject.name}-core"))

    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")

    library(libs.pcap4j.core)
    library(libs.pcap4j.static)
    library(libs.caffeine)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runServer {
        minecraftVersion("1.21.8")
    }
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    shadowJar {
        dependencies {
            exclude {
                println("${project.group} - $it.moduleGroup")
                // dependencies of common get included by the paper library loader
                it.moduleGroup != project.group
            }
        }
    }
}

tasks.build {
    dependsOn(tasks.named("shadowJar"))
}

bukkit {
    name = "SynSniff"
    main = "io.github.duckulus.synsniff.paper.SynSniffPlugin"
    apiVersion = "1.21"
    permissions {
        register("synsniff.command-fingerprint") {}
        register("synsniff.command-predictos") {}
    }
}
