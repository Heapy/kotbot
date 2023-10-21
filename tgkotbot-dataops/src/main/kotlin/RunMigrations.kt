@file:JvmName("RunMigrations")

import org.flywaydb.core.Flyway

fun main() {
    val env = System.getenv()

    val pgHost = env["KOTBOT_POSTGRES_HOST"]
    val pgPort = env["KOTBOT_POSTGRES_PORT"]
    val pgUser = env["KOTBOT_POSTGRES_USER"]
    val pgPassword = env["KOTBOT_POSTGRES_PASSWORD"]
    val pgDatabase = env["KOTBOT_POSTGRES_DATABASE"]

    Flyway
        .configure()
        .locations("classpath:migrations")
        .dataSource(
            "jdbc:postgresql://$pgHost:$pgPort/$pgDatabase",
            pgUser,
            pgPassword
        )
        .loggers("slf4j")
        .load()
        .migrate()
}
