package io.heapy.kotbot.infra.lifecycle

import io.heapy.komok.tech.di.lib.Module
import java.lang.management.ManagementFactory

@Module
class UptimeModule {
    val uptimeService by lazy {
        UptimeService()
    }
}

class UptimeService {
    val uptime: Long
        get() = ManagementFactory.getRuntimeMXBean().uptime
}
