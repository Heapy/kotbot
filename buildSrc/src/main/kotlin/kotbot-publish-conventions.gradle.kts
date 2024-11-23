plugins {
    signing
    `java-library`
    `maven-publish`
}

group = "io.heapy.kotbot"

java {
    withJavadocJar()
    withSourcesJar()
}

val modules: Map<String, Map<String, String>> = mapOf(
    "core" to mapOf(
        "publishName" to "Telegram chat bot framework",
        "publishDescription" to "Unopinionated and flexible library for building Telegram chat bots",
    ),
)

fun Project.getPublishName(): String = modules.getValue(name).getValue("publishName")
fun Project.getPublishDescription(): String = modules.getValue(name).getValue("publishDescription")

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.name

            from(components["java"])

            pom {
                name = project.getPublishName()
                description = project.getPublishDescription()
                url = "https://github.com/Heapy/kotbot"
                inceptionYear = "2018"
                licenses {
                    license {
                        name = "GPL-3.0-only"
                        url = "https://spdx.org/licenses/GPL-3.0-only.html"
                    }
                }
                developers {
                    developer {
                        id = "ruslan.ibrahimau"
                        name = "Ruslan Ibrahimau"
                        email = "ruslan@heapy.io"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/Heapy/kotbot.git"
                    developerConnection = "scm:git:ssh://github.com/Heapy/kotbot.git"
                    url = "https://github.com/Heapy/kotbot"
                }
            }
        }
    }

    repositories {
        maven {
            url = rootProject.layout.buildDirectory
                .dir("staging-deploy")
                .get().asFile.toURI()
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
