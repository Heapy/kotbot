package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.KotlinChatBotConfigurationModule
import io.heapy.kotbot.infra.openai.GptApiModule

@Module
open class CommandsModule(
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
    private val gptApiModule: GptApiModule,
) {
    open val commands by lazy {
        listOf(
            chatInfoCommand,
            spamCommand,
            startCommand,
            closeTopicCommand,
            gptCommand,
        ) + sendMessageToGroupCommands
    }

    open val gptCommand: Command by lazy {
        GptCommand(
            gptService = gptApiModule.gptService,
        )
    }

    open val chatInfoCommand: Command by lazy(::ChatInfoCommand)

    open val spamCommand: Command by lazy(::SpamCommand)

    open val closeTopicCommand: Command by lazy(::CloseTopicCommand)

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
