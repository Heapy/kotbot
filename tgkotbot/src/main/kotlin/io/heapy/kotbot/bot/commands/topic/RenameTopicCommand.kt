package io.heapy.kotbot.bot.commands.topic

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.commands.CommandExecutionContext
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.EditForumTopic
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.refLog
import io.heapy.kotbot.infra.jdbc.TransactionContext

class RenameTopicCommand(
    private val kotbot: Kotbot,
    private val notificationService: NotificationService,
) : Command {
    override val name = "/rename"
    override val requiredContext = listOf(Command.Context.GROUP_CHAT)
    override val requiredAccess = Command.Access.MODERATOR

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    override suspend fun execute() {
        val message = cex.message
        if (message.is_topic_message == true) {
            val threadId = message.message_thread_id

            if (threadId != null) {
                val newName = message.textWithoutCommand
                    ?: run {
                        log.info("No new name to rename")
                        return
                    }

                notificationService
                    .notifyAdmins(
                        """
                        Topic renamed in chat ${message.chat.title} by ${message.from?.refLog}
                        https://t.me/${message.chat.username}/${message.message_thread_id}
                        New name: $newName
                        """.trimIndent()
                    )

                kotbot.executeSafely(
                    EditForumTopic(
                        chat_id = LongChatId(message.chat.id),
                        message_thread_id = threadId,
                        name = newName,
                    )
                )
            } else {
                log.warn("No thread id to rename")
            }
        } else {
            log.warn("Not a topic message")
        }
    }

    private companion object : Logger()
}
