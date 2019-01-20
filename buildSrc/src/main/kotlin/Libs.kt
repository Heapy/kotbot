object Libs {
    private val kotlin = "1.3.11"
    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin"

    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.1.0"

    val tgBotApi = "org.telegram:telegrambots:4.1"

    private val ktor = "1.0.0"
    val ktorServer = "io.ktor:ktor-server-netty:$ktor"
    val ktorJackson = "io.ktor:ktor-jackson:$ktor"

    private val komodo = "0.0.1-dev-b37"
    val komodoLogging = "io.heapy.komodo.integration:komodo-slf4j:$komodo"
    val logback = "ch.qos.logback:logback-classic:1.3.0-alpha4"

    private val micrometerVersion = "1.1.0"
    val micrometer = "io.micrometer:micrometer-core:$micrometerVersion"
    val micrometerPrometheus = "io.micrometer:micrometer-registry-prometheus:$micrometerVersion"

    private val junit = "5.3.1"
    val junitApi = "org.junit.jupiter:junit-jupiter-api:$junit"
    val junitEngine = "org.junit.jupiter:junit-jupiter-engine:$junit"

    val mockk = "io.mockk:mockk:1.9"
}
