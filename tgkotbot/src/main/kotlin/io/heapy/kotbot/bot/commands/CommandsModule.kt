package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.KotlinChatBotConfigurationModule
import io.heapy.kotbot.bot.NotificationServiceModule
import io.heapy.kotbot.bot.commands.info.ChatInfoCommand
import io.heapy.kotbot.bot.commands.topic.CloseTopicCommand
import io.heapy.kotbot.bot.commands.topic.RenameTopicCommand
import io.heapy.kotbot.bot.commands.topic.ReopenTopicCommand
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.openai.GptApiModule

@Module
open class CommandsModule(
    private val kotbotModule: KotbotModule,
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
    private val gptApiModule: GptApiModule,
    private val notificationServiceModule: NotificationServiceModule,
) {
    open val commandResolver: CommandResolver by lazy {
        CommandResolver(
            commands = commands,
            commandExecutor = commandExecutor,
            admins = kotlinChatBotConfigurationModule.groupsConfiguration.admins.flatMap { it.value },
        )
    }

    open val commandExecutor: CommandExecutor by lazy {
        CommandExecutor(
            kotbot = kotbotModule.kotbot,
        )
    }

    open val commands by lazy {
        listOf(
            chatInfoCommand,
            spamCommand,
            startCommand,
            closeTopicCommand,
            renameTopicCommand,
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

    open val closeTopicCommand: Command by lazy {
        CloseTopicCommand(
            notificationService = notificationServiceModule.notificationService,
        )
    }

    open val renameTopicCommand: Command by lazy {
        RenameTopicCommand(
            notificationService = notificationServiceModule.notificationService,
        )
    }

    open val reopenTopicCommand: Command by lazy {
        ReopenTopicCommand(
            notificationService = notificationServiceModule.notificationService,
        )
    }

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
