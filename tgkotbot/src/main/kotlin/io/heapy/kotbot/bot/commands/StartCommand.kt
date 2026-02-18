package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.commands.Command.Access
import io.heapy.kotbot.bot.commands.Command.Context
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.infra.jdbc.TransactionContext

class StartCommand(
    private val kotbot: Kotbot,
) : Command {
    override val name = "/start"
    override val requiredContext = listOf(Context.USER_CHAT)
    override val requiredAccess = Access.USER

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    override suspend fun execute() {
        val message = cex.message
        kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.chat.id),
                text = startMessage(
                    languageCode = message.from?.language_code,
                ),
                parse_mode = ParseMode.MarkdownV2.name,
            )
        )
    }

    private companion object : Logger()
}
