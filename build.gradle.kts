plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.8.0"
}

group = "com.vdian"
version = "1.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo.spring.io/release")
    }
    maven {
        url = uri("https://repository.jboss.org/maven2")
    }
    maven {
        url = uri("https://repository.mulesoft.org/nexus/content/repositories/public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2021.3.3")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(
        /* Plugin Dependencies */

    ))
}
dependencies {
    implementation("com.github.spullara.mustache.java:compiler:0.9.5")
}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("213")
        untilBuild.set("223.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
