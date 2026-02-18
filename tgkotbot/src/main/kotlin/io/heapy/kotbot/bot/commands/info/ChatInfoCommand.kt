package io.heapy.kotbot.bot.commands.info

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.commands.CommandExecutionContext
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.Chat
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.infra.debug.PrettyPrint
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown

class ChatInfoCommand(
    private val kotbot: Kotbot,
    private val prettyPrint: PrettyPrint,
    private val markdown: Markdown,
) : Command {
    override val name = "/chat_info"
    override val requiredContext = listOf(Command.Context.GROUP_CHAT, Command.Context.USER_CHAT)
    override val requiredAccess = Command.Access.ADMIN

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    override suspend fun execute() {
        val message = cex.message
        kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.chat.id),
                parse_mode = ParseMode.MarkdownV2.name,
                text = markdown.escape(
                    buildString {
                        appendLine("```json")
                        appendLine(prettyPrint.convert(Chat.serializer(), message.chat))
                        appendLine("```")
                    }
                )
            )
        )
    }
}
