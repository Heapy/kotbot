package io.heapy.kotbot.bot.commands.topic

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.EditForumTopic
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update

class RenameTopicCommand(
    private val notificationService: NotificationService,
) : Command {
    override val name = "/rename"
    override val context = listOf(Command.Context.GROUP_CHAT)
    override val access = Command.Access.MODERATOR
    override val deleteCommandMessage = true

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
        message: Message,
    ) {
        if (message.chat.is_forum != true) {
            log.info("Chat is not a forum")
            return
        }

        notificationService
            .notifyAdmins(
                """
                    Topic renamed in chat ${message.chat.title} by ${message.from?.username}
                    https://t.me/${message.chat.id}/${message.message_thread_id}
                    New name: ${message.textWithoutCommand}
                """.trimIndent()
            )

        val newName = message.textWithoutCommand
            ?: run {
                log.info("No new name to rename")
                return
            }

        val threadId = message.message_thread_id

        if (threadId != null) {
            kotbot.executeSafely(
                EditForumTopic(
                    chat_id = LongChatId(message.chat.id),
                    message_thread_id = threadId,
                    name = newName,
                )
            )
        } else {
            log.info("No thread id to rename")
        }
    }

    private companion object : Logger()
}
