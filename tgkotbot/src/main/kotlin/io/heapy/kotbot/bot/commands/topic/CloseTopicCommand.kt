package io.heapy.kotbot.bot.commands.topic

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.CloseForumTopic
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.infra.logger

class CloseTopicCommand(
    private val notificationService: NotificationService,
) : Command {
    override val name = "/close"
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
                    Topic closed in chat ${message.chat.title} by ${message.from?.username}
                    https://t.me/${message.chat.id}/${message.message_thread_id}
                """.trimIndent()
            )

        if (threadId != null) {
            kotbot.executeSafely(
                CloseForumTopic(
                    chat_id = LongChatId(message.chat.id),
                    message_thread_id = threadId,
                )
            )
        } else {
            log.info("No thread id to close")
        }
    }

    private companion object {
        private val log = logger<CloseTopicCommand>()
    }
}
