package io.heapy.kotbot.bot.commands.topic

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.ReopenForumTopic
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update

class ReopenTopicCommand(
    private val notificationService: NotificationService,
): Command {
    override val name = "/reopen"
    override val context = listOf(Command.Context.GROUP_CHAT)
    override val access = Command.Access.MODERATOR
    override val deleteCommandMessage = true

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
        message: Message,
    ) {
        val threadId = message.message_thread_id

        notificationService
            .notifyAdmins(
                """
                    Topic reopened in chat ${message.chat.title} by ${message.from?.username}
                    https://t.me/${message.chat.id}/${message.message_thread_id}
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
    }

    private companion object : Logger()
}
