package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update

class SendMessageFromBotCommand(
    private val admin: Long,
    override val name: String,
    val id: Long,
) : Command {
    override val context = listOf(Command.Context.USER_CHAT)
    override val access = Command.Access.ADMIN
    override val deleteCommandMessage = false

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
        message: Message,
    ) {
        message.textWithoutCommand?.let { text ->
            kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(id),
                    text = text
                )
            )
            kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(admin),
                    text = """
                    ${message.from?.username} sent following message to chat $name:
                    $text
                """.trimIndent()
                )
            )
        }
    }
}
