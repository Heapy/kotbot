package io.heapy.kotbot.bot.events

import io.heapy.komok.tech.di.lib.Module

@Module
class EventsModule {
    val route by lazy {
        EventsRoute()
    }
}
