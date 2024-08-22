package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.commands.Command.Access
import io.heapy.kotbot.bot.commands.Command.Context
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.infra.readResource

class StartCommand : Command {
    override val name = "/start"
    override val context = listOf(Context.USER_CHAT)
    override val access = Access.USER
    override val deleteCommandMessage = false

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
        message: Message,
    ) {
        start_text?.let { text ->
            kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(message.chat.id),
                    text = text,
                    parse_mode = "MarkdownV2",
                )
            )
        }
    }

    companion object {
        private val start_text = readResource("start.txt")
    }
}
