package io.heapy.tgpt.infra.lifecycle

import io.heapy.komok.tech.di.lib.Module
import io.heapy.komok.tech.logging.Logger
import java.util.concurrent.atomic.AtomicBoolean

@Module
open class AutoClosableModule : AutoCloseable {
    private val closable = mutableListOf<() -> Unit>()
    private val used = AtomicBoolean(false)

    fun <T> addClosable(
        t: T,
        close: (T) -> Unit,
    ): T {
        log.info("Adding closable $t")
        closable.add {
            log.info("Closing $t")
            close(t)
            log.info("Closed $t")
        }
        return t
    }

    override fun close() {
        if (used.compareAndSet(false, true)) {
            closable
                .reversed()
                .forEach { closeFunction ->
                    try {
                        closeFunction()
                    } catch (e: Exception) {
                        log.error("Error while closing resource", e)
                    }
                }
            log.info("Closed all resources")
        } else {
            log.warn("Already closed")
        }
    }

    private companion object : Logger()
}
