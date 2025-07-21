plugins {
    `java-library`
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("de.eldoria.plugin-yml.bukkit") version "0.7.1"
}

group = "io.github.duckulus"
version = "1.0"
description = "Passive TCP/IP stack fingerprinting for minecraft"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")

    library("org.pcap4j:pcap4j-core:1.8.2")
    library("org.pcap4j:pcap4j-packetfactory-static:1.8.2")

    library("com.github.ben-manes.caffeine:caffeine:3.2.0")
}


java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withJavadocJar()
    withSourcesJar()
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
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

bukkit {
    main = "de.duckulus.synsniff.SynSniff"
    apiVersion = "1.21"
    permissions {
        register("synsniff.command-fingerprint") {}
        register("synsniff.command-predictos") {}
    }
}