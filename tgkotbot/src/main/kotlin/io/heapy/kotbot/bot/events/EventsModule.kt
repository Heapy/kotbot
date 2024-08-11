package io.heapy.kotbot.bot.events

import io.heapy.komok.tech.di.lib.Module

@Module
open class EventsModule {
    open val route by lazy {
        EventsRoute()
    }
}
