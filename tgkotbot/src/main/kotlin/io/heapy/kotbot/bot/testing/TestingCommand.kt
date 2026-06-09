package io.heapy.kotbot.bot.testing

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.commands.Command
import io.heapy.kotbot.bot.commands.CommandExecutionContext
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.join.ChallengeAttemptDao
import io.heapy.kotbot.bot.join.JoinSessionDao
import io.heapy.kotbot.bot.join.VerifiedUserDao
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.infra.configuration.TestingConfiguration
import io.heapy.kotbot.infra.jdbc.TransactionContext

/**
 * Admin-only testing helper (`/test`, private chat). Wipes the configured test user's join-flow
 * state — challenge attempts, join sessions, verified-user record — so the join/CAS/appeal flow can
 * be replayed from scratch, then flips the test user's forced CAS verdict (flagged ⇄ clean) so the
 * next replay exercises the opposite path. The test user is testing.testUserId
 * (env KOTBOT_TEST_USER_ID); a no-op reply is sent when it is unset.
 */
class TestingCommand(
    private val kotbot: Kotbot,
    private val testingConfiguration: TestingConfiguration,
    private val testingCasClientToggle: TestingCasClientToggle,
    private val verifiedUserDao: VerifiedUserDao,
    private val joinSessionDao: JoinSessionDao,
    private val challengeAttemptDao: ChallengeAttemptDao,
) : Command {
    override val name = "/test"
    override val requiredContext = listOf(Command.Context.USER_CHAT)
    override val requiredAccess = Command.Access.ADMIN

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    override suspend fun execute() {
        val chatId = cex.message.chat.id
        val testUserId = testingConfiguration.testUserId

        if (testUserId == null) {
            reply(chatId, "No test user configured. Set KOTBOT_TEST_USER_ID to use /test.")
            return
        }

        // FK order: challenge_attempt references join_session, so delete attempts first.
        val attempts = challengeAttemptDao.deleteByTelegramId(testUserId)
        val sessions = joinSessionDao.deleteByTelegramId(testUserId)
        val verified = verifiedUserDao.deleteByTelegramId(testUserId)

        testingCasClientToggle.toggle()
        val flagged = testingCasClientToggle.get()

        log.info(
            "/test reset test user {}: removed {} attempts, {} sessions, {} verified rows; CAS now {}",
            testUserId, attempts, sessions, verified, if (flagged) "FLAGGED" else "CLEAN",
        )

        reply(
            chatId,
            buildString {
                appendLine("Reset test user $testUserId:")
                appendLine("• challenge attempts removed: $attempts")
                appendLine("• join sessions removed: $sessions")
                appendLine("• verified-user rows removed: $verified")
                append("CAS verdict for this user is now: ${if (flagged) "FLAGGED" else "CLEAN"}")
            },
        )
    }

    private suspend fun reply(
        chatId: Long,
        text: String,
    ) {
        val _ = kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(chatId),
                text = text,
            )
        )
    }

    private companion object : Logger()
}
