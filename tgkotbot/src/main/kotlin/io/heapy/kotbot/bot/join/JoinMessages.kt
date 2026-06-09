package io.heapy.kotbot.bot.join

import io.heapy.kotbot.bot.ApproveAppealCallbackData
import io.heapy.kotbot.bot.DeclineAppealCallbackData
import io.heapy.kotbot.bot.JoinChallengeAnswerCallbackData
import io.heapy.kotbot.bot.cas.CasResult
import io.heapy.kotbot.bot.model.InlineKeyboardButton
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.User
import io.heapy.kotbot.bot.ref
import io.heapy.kotbot.bot.use_case.callback.CallbackDataService
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown
import java.util.UUID
import kotlin.time.Duration

/**
 * Presentation layer for the join/verification flow: renders the user- and admin-facing text
 * and the inline keyboards. Keeping it separate makes the formatting unit-testable in isolation
 * and keeps the stage handlers focused on orchestration.
 */
class JoinMessages(
    private val markdown: Markdown,
    private val callbackDataService: CallbackDataService,
) {
    fun formatChallengeMessage(
        snippet: String,
        maxAttempts: Int,
        attemptsUsed: Int,
        prefix: String = "",
    ): String = markdown.escape(buildString {
        append(prefix)
        appendLine("To join the chat, please solve this Kotlin challenge:")
        appendLine()
        appendLine("What is the output of the following code?")
        appendLine()
        appendLine("```kotlin")
        appendLine(snippet)
        appendLine("```")
        appendLine()
        append("Attempts: $attemptsUsed/$maxAttempts")
    })

    fun formatCorrect(): String =
        markdown.escape("✅ Correct! Your join request has been approved. Welcome!")

    fun formatExhausted(attemptsUsed: Int): String =
        markdown.escape("❌ Wrong answer. You've used all $attemptsUsed attempts. Your join request has been declined.")

    fun formatCooldown(remaining: Duration): String =
        markdown.escape("❌ You can retry the challenge in $remaining. Please try again later.")

    fun formatCasFlaggedMessage(
        casResult: CasResult.Flagged,
    ): String = markdown.escape(buildString {
        appendLine("✅ Challenge passed.")
        appendLine()
        append("⚠️ You're listed in the CAS ban database")
        casDetails(casResult.offenses, casResult.timeAdded)?.let { append(" ($it)") }
        appendLine(".")
        appendLine()
        append("You'll be admitted in read-only mode. If you think this is a mistake, reply here with a short explanation to appeal for write access.")
    })

    /** Not escaped on purpose: [io.heapy.kotbot.bot.NotificationService.notifyAdmins] escapes it. */
    fun formatAppealForAdmins(
        session: JoinSessionData,
        from: User,
        text: String,
    ): String = buildString {
        appendLine("📝 Write-access appeal from ${from.ref}")
        append("CAS")
        casDetails(session.casOffenses, session.casTimeAdded)?.let { append(" ($it)") }
        appendLine()
        appendLine("Chat: ${session.chatId}")
        session.casMessages?.takeIf { it.isNotEmpty() }?.let { messages ->
            appendLine()
            appendLine("Banned for:")
            appendLine(messages.joinToString("\n\n"))
        }
        appendLine()
        appendLine("Explanation:")
        append(text)
    }

    private fun casDetails(offenses: Int?, timeAdded: String?): String? =
        listOfNotNull(
            offenses?.let { "offenses: $it" },
            timeAdded?.let { "added: $it" },
        ).takeIf { it.isNotEmpty() }?.joinToString(", ")

    context(_: TransactionContext)
    suspend fun buildAnswerKeyboard(
        challengeId: UUID,
        options: List<String>,
    ): InlineKeyboardMarkup = InlineKeyboardMarkup(
        inline_keyboard = options.mapIndexed { index, option ->
            listOf(
                InlineKeyboardButton(
                    text = option,
                    callback_data = callbackDataService.insert(
                        JoinChallengeAnswerCallbackData(
                            challengeId = challengeId.toString(),
                            selectedIndex = index,
                        )
                    ),
                )
            )
        },
    )

    context(_: TransactionContext)
    suspend fun buildAppealKeyboard(
        sessionId: Long,
    ): InlineKeyboardMarkup? {
        val approveData = callbackDataService.insert(ApproveAppealCallbackData(sessionId))
        val declineData = callbackDataService.insert(DeclineAppealCallbackData(sessionId))
        return if (approveData != null && declineData != null) {
            InlineKeyboardMarkup(
                inline_keyboard = listOf(
                    listOf(
                        InlineKeyboardButton(text = "✅ Approve", callback_data = approveData),
                        InlineKeyboardButton(text = "❌ Decline", callback_data = declineData),
                    )
                )
            )
        } else null
    }
}
