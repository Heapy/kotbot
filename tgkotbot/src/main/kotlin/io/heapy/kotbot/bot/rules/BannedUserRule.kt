package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.bot.anyMessage
import io.heapy.kotbot.bot.banFrom
import io.heapy.kotbot.bot.delete
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.refLog
import io.heapy.kotbot.database.enums.TelegramUserStatus
import io.heapy.kotbot.infra.jdbc.TransactionContext

class BannedUserRule : Rule {
    context(_: TransactionContext, _: RuleContext)
    override suspend fun validate(
        kotbot: Kotbot,
        update: TypedUpdate,
        actions: Actions,
    ) {
        val message = update.anyMessage ?: return
        val chatType = message.chat.type
        if (chatType != "group" && chatType != "supergroup") return

        val userContext = userContext ?: return

        if (userContext.status == TelegramUserStatus.BANNED) {
            log.info("Banned user ${message.from?.refLog} sent message in chat ${message.chat.id}, deleting and banning")
            actions.runIfNew("banned_user_rule", message.delete) {
                val _ = kotbot.executeSafely(it)
            }
            actions.runIfNew("banned_user_rule", message.banFrom) {
                val _ = kotbot.executeSafely(it)
            }
        }
    }

    private companion object : Logger()
}
