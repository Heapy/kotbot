package io.heapy.kotbot.infra.debug

import io.heapy.komok.tech.di.lib.Module
import kotlinx.serialization.json.Json

@Module
open class PrettyPrintModule {
    open val prettyPrint by lazy {
        PrettyPrint(
            json = json,
        )
    }

    open val json by lazy {
        Json {
            prettyPrint = true
        }
    }
}
