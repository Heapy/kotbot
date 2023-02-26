package io.heapy.kotbot.bot

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.readLines

fun dotenv(
    override: Map<String, String> = emptyMap(),
): Map<String, String> {
    return resolveEnv(Paths.get(".").toAbsolutePath().normalize())
        ?.let { envPath ->
            buildMap {
                putAll(System.getenv())
                putAll(
                    envPath.readLines()
                        .filter(String::isNotBlank)
                        .filter { !it.startsWith("#") }
                        .associate {
                            val split = it.split("=", limit = 2)
                            if (split.size != 2) error("Line syntax: key=value, got: $it")
                            split[0].trim() to split[1].trim()
                        }
                )
                putAll(override)
            }
        }
        ?: buildMap {
            putAll(System.getenv())
            putAll(override)
        }
}

private fun resolveEnv(root: Path): Path? {
    return root.resolve(".env").let { envPath ->
        if (envPath.exists()) {
            envPath
        } else {
            if (envPath.contains(envPath.resolve(".git"))) {
                null
            } else {
                resolveEnv(root = root.parent)
            }
        }
    }
}
