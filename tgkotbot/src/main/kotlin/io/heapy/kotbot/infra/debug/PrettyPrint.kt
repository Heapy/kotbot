package io.heapy.kotbot.infra.debug

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

class PrettyPrint(
    private val json: Json,
) {
    fun <T> convert(serializer: SerializationStrategy<T>, value: T): String {
        return json.encodeToString(serializer, value)
    }
}
