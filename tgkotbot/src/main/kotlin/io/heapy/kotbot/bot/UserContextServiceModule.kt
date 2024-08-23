package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module

@Module
open class UserContextServiceModule(
    private val notificationServiceModule: NotificationServiceModule,
) {
    open val userContextService: UserContextService by lazy {
        UserContextService(
            notificationService = notificationServiceModule.notificationService,
        )
    }
}
