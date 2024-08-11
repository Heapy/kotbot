package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.KotlinChatBotConfigurationModule

@Module
open class CommandsModule(
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
) {
    open val commands by lazy {
        listOf(
            chatInfoCommand,
            spamCommand,
            startCommand,
        ) + sendMessageToGroupCommands
    }

    open val chatInfoCommand: Command by lazy(::ChatInfoCommand)

    open val spamCommand: Command by lazy(::SpamCommand)

    open val startCommand: Command by lazy(::StartCommand)

    open val sendMessageToGroupCommands by lazy {
        kotlinChatBotConfigurationModule.groupsConfiguration.allowedGroups.map { (name, id) ->
            SendMessageFromBotCommand(
                admin = kotlinChatBotConfigurationModule.groupsConfiguration.adminPrivateGroup,
                name = "/$name",
                id = id,
            )
        }
    }
}
