package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.commands.Command.Access
import io.heapy.kotbot.bot.commands.Command.Context
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update

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

    }
}
