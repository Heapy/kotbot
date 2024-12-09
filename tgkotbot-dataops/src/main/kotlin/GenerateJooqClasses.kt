@file:JvmName("GenerateJooqClasses")

import org.flywaydb.core.Flyway
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.postgresql.ds.PGSimpleDataSource
import java.util.*

fun main() {
    drop()
    flyway()
    jooq()
}

fun drop() {
    PGSimpleDataSource()
        .apply {
            setURL("jdbc:postgresql://localhost:9577/kotbot")
            user = "kotbot"
            password = "kotbot"
        }
        .connection
        .use { connection ->
            connection.createStatement().use { statement ->
                statement.execute("DROP SCHEMA public CASCADE;")
                statement.execute("CREATE SCHEMA public;")
            }
        }
}

fun flyway() {
    Flyway
        .configure()
        .locations(
            "filesystem:./tgkotbot-dataops/src/main/resources/migrations",
            "filesystem:./tgkotbot-dataops/src/main/resources/init",
        )
        .dataSource(
            "jdbc:postgresql://localhost:9577/kotbot",
            "kotbot",
            "kotbot"
        )
        .loggers("slf4j")
        .load()
        .migrate()
}

fun jooq() {
    GenerationTool.generate(Configuration().apply {
        jdbc = Jdbc().apply {
            driver = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:9577/kotbot"
            user = "kotbot"
            password = "kotbot"
        }

        generator = Generator().apply {
            name = "org.jooq.codegen.KotlinGenerator"
            database = Database().apply {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                includes = ".*"
                excludes = "flyway_schema_history"
                inputSchema = "public"
            }

            generate = Generate().apply {
                isDaos = true
                isPojos = true
                isKotlinNotNullPojoAttributes = true
                isKotlinNotNullInterfaceAttributes = true
                isKotlinNotNullRecordAttributes = true
                isImmutablePojos = true
                isInterfaces = true
                isImmutableInterfaces = true
            }

            target = Target().apply {
                packageName = "io.heapy.kotbot.database"
                directory = "./tgkotbot-database/src/main/kotlin"
                locale = Locale.ROOT.toLanguageTag()
            }
        }
    })
}
