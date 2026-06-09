package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.dao.DaoModule

@Module
class UserContextServiceModule(
    private val notificationServiceModule: NotificationServiceModule,
    private val daoModule: DaoModule,
) {
    val userContextService: UserContextService by lazy {
        UserContextService(
            notificationService = notificationServiceModule.notificationService,
            userContextDao = daoModule.userContextDao,
        )
    }
}
