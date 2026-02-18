package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.refLog
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown

class SendMessageFromBotCommand(
    private val kotbot: Kotbot,
    private val notificationService: NotificationService,
    private val id: Long,
    private val markdown: Markdown,
    override val name: String,
) : Command {
    override val requiredContext = listOf(Command.Context.USER_CHAT)
    override val requiredAccess = Command.Access.ADMIN

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    override suspend fun execute() {
        val message = cex.message
        message.textWithoutCommand?.let { text ->
            val escaped = markdown.escape(text)
            kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(id),
                    parse_mode = ParseMode.MarkdownV2.name,
                    text = escaped,
                )
            )
            notificationService
                .notifyAdmins(
                    message = """
                    ${message.from?.refLog} sent following message to chat $name:
                    $text
                    """.trimIndent()
                )
        }
    }
}
