import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
    signing
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

mavenPublishing {
    coordinates("io.github.duckulus", "syn-sniff", rootProject.version.toString())

    publishToMavenCentral()
    signAllPublications()

    configure(
        JavaLibrary(
            javadocJar = JavadocJar.Javadoc(),
            sourcesJar = true
        )
    )

    pom {
        name.set(rootProject.name)
        description.set(rootProject.description)
        url.set("https://github.com/Duckulus/syn-sniff/")
        licenses {
            license {
                name.set("MIT License")
                url.set("http://www.opensource.org/licenses/mit-license.php")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("Duckulus")
                name.set("Amin Haddou")
                url.set("https://duckul.us/")
            }
        }
        scm {
            url.set("https://github.com/Duckulus/syn-sniff/")
            connection.set("scm:git:git://github.com/Duckulus/syn-sniff.git")
            developerConnection.set("scm:git:ssh://git@github.com/Duckulus/syn-sniff.git")
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