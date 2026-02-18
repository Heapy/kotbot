package io.heapy.tgpt.infra.lifecycle

import io.heapy.komok.tech.di.lib.Module
import java.lang.management.ManagementFactory

@Module
open class UptimeModule {
    open val uptimeService by lazy {
        UptimeService()
    }
}

class UptimeService {
    val uptime: Long
        get() = ManagementFactory.getRuntimeMXBean().uptime
}
