package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.banFrom
import io.heapy.kotbot.bot.delete
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update

class SpamCommand : Command {
    override val name = "/spam"
    override val context = listOf(Command.Context.GROUP_CHAT)
    override val access = Command.Access.MODERATOR
    override val deleteCommandMessage = true

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
        message: Message,
    ) {
        message.reply_to_message?.let { reply ->
            kotbot.executeSafely(reply.delete)
            kotbot.executeSafely(reply.banFrom)
        }
    }
}
