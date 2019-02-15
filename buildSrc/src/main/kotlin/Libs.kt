object Libs {
    const val kotlinVersion = "1.3.21"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"

    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.1.1"

    const val tgBotApi = "org.telegram:telegrambots:4.1.2"

    private const val ktor = "1.1.2"
    const val ktorServer = "io.ktor:ktor-server-netty:$ktor"
    const val ktorJackson = "io.ktor:ktor-jackson:$ktor"

    private const val komodo = "0.0.1-dev-b37"
    const val komodoLogging = "io.heapy.komodo.integration:komodo-slf4j:$komodo"
    const val logback = "ch.qos.logback:logback-classic:1.3.0-alpha4"

    private const val micrometerVersion = "1.1.3"
    const val micrometer = "io.micrometer:micrometer-core:$micrometerVersion"
    const val micrometerPrometheus = "io.micrometer:micrometer-registry-prometheus:$micrometerVersion"

    private const val junit = "5.3.1"
    const val junitApi = "org.junit.jupiter:junit-jupiter-api:$junit"
    const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:$junit"

    const val mockk = "io.mockk:mockk:1.9"
}
