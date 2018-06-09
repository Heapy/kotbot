plugins {
    application
    kotlin("jvm") version "1.2.41"
}

val kotlinVersion: String by project
val tgBotsApiVersion: String by project
val mockkVersion: String by project
val junitVersion: String by project

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.telegram:telegrambots:$tgBotsApiVersion")

    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testCompile("io.mockk:mockk:$mockkVersion")
}

repositories {
    jcenter()
}

application {
    applicationName = "kotbot"
    mainClassName = "link.kotlin.kotbot.Application"
}
