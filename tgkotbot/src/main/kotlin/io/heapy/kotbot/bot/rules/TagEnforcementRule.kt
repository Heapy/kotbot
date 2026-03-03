package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.bot.anyMessage
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SetChatMemberTag
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.refLog
import io.heapy.kotbot.infra.jdbc.TransactionContext

class TagEnforcementRule : Rule {
    context(_: TransactionContext, _: RuleContext)
    override suspend fun validate(
        kotbot: Kotbot,
        update: TypedUpdate,
        actions: Actions,
    ) {
        val message = update.anyMessage ?: return
        val chatType = message.chat.type
        if (chatType != "group" && chatType != "supergroup") return

        val userId = message.from?.id ?: return
        val userContext = userContext ?: return

        val expectedTag = userContext.badge
        val currentTag = message.sender_tag

        if (expectedTag != currentTag) {
            log.info("Updating tag for user ${message.from?.refLog} in chat ${message.chat.id}: '$currentTag' -> '$expectedTag'")
            kotbot.executeSafely(
                SetChatMemberTag(
                    chat_id = LongChatId(message.chat.id),
                    user_id = userId,
                    tag = expectedTag,
                ),
            )
        }
    }

    private companion object : Logger()
}
