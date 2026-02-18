package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.KotlinChatBotConfigurationModule
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.infra.markdown.MarkdownModule
import io.heapy.kotbot.bot.NotificationServiceModule
import io.heapy.kotbot.bot.UserContextServiceModule
import io.heapy.kotbot.bot.commands.info.ChatInfoCommand
import io.heapy.kotbot.bot.commands.topic.CloseTopicCommand
import io.heapy.kotbot.bot.commands.topic.RenameTopicCommand
import io.heapy.kotbot.bot.commands.topic.ReopenTopicCommand
import io.heapy.kotbot.bot.use_case.callback.CallbackDataServiceModule
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.debug.PrettyPrintModule
import io.heapy.kotbot.infra.openai.GptApiModule

@Module
open class CommandsModule(
    private val kotbotModule: KotbotModule,
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
    private val gptApiModule: GptApiModule,
    private val callbackDataServiceModule: CallbackDataServiceModule,
    private val notificationServiceModule: NotificationServiceModule,
    private val userContextServiceModule: UserContextServiceModule,
    private val prettyPrintModule: PrettyPrintModule,
    private val markdownModule: MarkdownModule,
    private val daoModule: DaoModule,
) {
    open val commandResolver: CommandResolver by lazy {
        CommandResolver(
            commands = commands,
            commandExecutor = commandExecutor,
            userContextService = userContextServiceModule.userContextService,
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
            reopenTopicCommand,
            renameTopicCommand,
            gptCommand,
        ) + sendMessageToGroupCommands
    }

    open val gptCommand: Command by lazy {
        GptCommand(
            kotbot = kotbotModule.kotbot,
            gptService = gptApiModule.gptService,
            markdown = markdownModule.markdown,
            callbackDataService = callbackDataServiceModule.callbackDataService,
            gptSessionDao = daoModule.gptSessionDao,
        )
    }

    open val chatInfoCommand: Command by lazy {
        ChatInfoCommand(
            prettyPrint = prettyPrintModule.prettyPrint,
            kotbot = kotbotModule.kotbot,
            markdown = markdownModule.markdown,
        )
    }

    open val spamCommand: Command by lazy {
        SpamCommand(
            kotbot = kotbotModule.kotbot,
            notificationChannelId = kotlinChatBotConfigurationModule.groupsConfiguration.notificationChannel,
            callbackDataService = callbackDataServiceModule.callbackDataService,
            markdown = markdownModule.markdown,
        )
    }

    open val closeTopicCommand: Command by lazy {
        CloseTopicCommand(
            kotbot = kotbotModule.kotbot,
            notificationService = notificationServiceModule.notificationService,
        )
    }

    open val renameTopicCommand: Command by lazy {
        RenameTopicCommand(
            kotbot = kotbotModule.kotbot,
            notificationService = notificationServiceModule.notificationService,
        )
    }

    open val reopenTopicCommand: Command by lazy {
        ReopenTopicCommand(
            kotbot = kotbotModule.kotbot,
            notificationService = notificationServiceModule.notificationService,
        )
    }

    open val startCommand: Command by lazy {
        StartCommand(
            kotbot = kotbotModule.kotbot,
        )
    }

    open val sendMessageToGroupCommands by lazy {
        kotlinChatBotConfigurationModule
            .groupsConfiguration
            .allowedGroups
            .map { (name, id) ->
                SendMessageFromBotCommand(
                    kotbot = kotbotModule.kotbot,
                    notificationService = notificationServiceModule.notificationService,
                    name = "/$name",
                    id = id,
                    markdown = markdownModule.markdown,
                )
            }
    }
}
