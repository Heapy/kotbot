package io.heapy.kotbot.bot.commands.topic

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.commands.CommandExecutionContext
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.ReopenForumTopic
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.refLog
import io.heapy.kotbot.infra.jdbc.TransactionContext

class ReopenTopicCommand(
    private val notificationService: NotificationService,
    private val kotbot: Kotbot,
): Command {
    override val name = "/reopen"
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

            notificationService
                .notifyAdmins(
                    """
                    Topic reopened in chat ${message.chat.title} by ${message.from?.refLog}
                    https://t.me/${message.chat.username}/${message.message_thread_id}
                    """.trimIndent()
                )

            if (threadId != null) {
                kotbot.executeSafely(
                    ReopenForumTopic(
                        chat_id = LongChatId(message.chat.id),
                        message_thread_id = threadId,
                    )
                )
            } else {
                log.info("No thread id to reopen")
            }
        } else {
            log.info("Not a topic message")
        }
    }

    private companion object : Logger()
}
