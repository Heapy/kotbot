package io.heapy.kotbot.infra.debug

import io.heapy.komok.tech.di.lib.Module
import kotlinx.serialization.json.Json

@Module
class PrettyPrintModule {
    val prettyPrint by lazy {
        PrettyPrint(
            json = json,
        )
    }

    val json by lazy {
        Json {
            prettyPrint = true
        }
    }
}
