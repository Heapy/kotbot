@file:JvmName("RunMigrations")

import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun main() {
    val env = System.getenv()

    val pgHost = env["TGPT_POSTGRES_HOST"]
    val pgPort = env["TGPT_POSTGRES_PORT"]
    val pgUser = env["TGPT_POSTGRES_USER"]
    val pgPassword = env["TGPT_POSTGRES_PASSWORD"]
    val pgDatabase = env["TGPT_POSTGRES_DATABASE"]

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

fun runMigrations(
    dataSource: DataSource,
) {
    Flyway
        .configure()
        .locations("classpath:migrations")
        .dataSource(dataSource)
        .loggers("slf4j")
        .load()
        .migrate()
}
