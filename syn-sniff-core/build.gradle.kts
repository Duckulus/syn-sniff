import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar

plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
    signing
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.pcap4j.core)
    implementation(libs.pcap4j.static)
    implementation(libs.caffeine)

    compileOnly("net.kyori:adventure-api:4.22.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.22.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
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
