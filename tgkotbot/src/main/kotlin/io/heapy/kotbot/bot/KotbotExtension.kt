package io.heapy.kotbot.bot

import io.heapy.komok.tech.logging.logger

private val log = logger<Kotbot>()

internal suspend fun <Request : Method<Request, Result>, Result> Kotbot.executeSafely(
    method: Request,
): Result? {
    return try {
        execute(method)
    } catch (e: Exception) {
        log.error("Method {} failed: {}", method, e.message, e)
        null
    }
}
