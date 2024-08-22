package io.heapy.kotbot.bot.commands.info

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update

class ChatInfoCommand : Command {
    override val name = "/chat-info"
    override val context = listOf(Command.Context.GROUP_CHAT, Command.Context.USER_CHAT)
    override val access = Command.Access.ADMIN
    override val deleteCommandMessage = true

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
        message: Message,
    ) {
        kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.chat.id),
                text = """
                    Chat id: ${message.chat.id}
                """.trimIndent()
            )
        )
    }
}
