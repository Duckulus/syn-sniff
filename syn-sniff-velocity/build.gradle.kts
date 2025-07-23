plugins {
    `java-library`
    id("com.gradleup.shadow") version "8.3.8"
    id("xyz.jpenilla.resource-factory-velocity-convention") version "1.3.0"
    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation(project(":${rootProject.name}-core"))

    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runVelocity {
        velocityVersion("3.4.0-SNAPSHOT")
    }
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    shadowJar {
        val shadeBase = "io.github.duckulus.synsniff.shaded"
        relocate("org.pcap4j", "$shadeBase.pcap4j")
        relocate("com.github.benmanes.caffeine", "$shadeBase.caffeine")
    }
}

tasks.build {
    dependsOn(tasks.named("shadowJar"))
}

velocityPluginJson {
    id = "syn-sniff"
    name = "SynSniff"
    description = project.description
    main = "io.github.duckulus.synsniff.velocity.SynSniffVelocityPlugin"
    version = project.version.toString()
    authors = listOf("Duckulus")
}
