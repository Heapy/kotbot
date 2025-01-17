package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.*
import io.heapy.kotbot.bot.dao.GarbageMessageDao
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.database.enums.ActionType
import io.heapy.kotbot.database.enums.MatchType

class DeleteGarbageRule(
    private val userContextService: UserContextService,
    private val garbageMessageDao: GarbageMessageDao,
) : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) = garbageMessageDao.transaction {
        update.anyText { messageText, message ->
            val possibleActions = garbageMessageDao
                .getGarbageMessages()
                .filter { garbageMessage ->
                    when (garbageMessage.match) {
                        MatchType.EXACT -> messageText.lowercase() == garbageMessage.text
                        MatchType.CONTAINS -> messageText.contains(garbageMessage.text)
                        MatchType.CONTAINS_WORD -> messageText.contains(wordRegex(garbageMessage.text))
                    }
                }
                .groupBy { garbageMessages -> garbageMessages.action }

            val ban = possibleActions[ActionType.BAN]?.first()
            val warn = possibleActions[ActionType.WARN]?.first()

            when {
                ban != null -> {
                    log.info("Delete message ${message.text}, reason ${ban.type}, from ${message.from?.info} (banned)")
                    userContextService.ban(message, ban.type)
                    actions.runIfNew("garbage_ban_rule", message.delete) {
                        kotbot.executeSafely(it)
                    }
                    actions.runIfNew("garbage_ban_rule", message.banFrom) {
                        kotbot.executeSafely(it)
                    }
                }

                warn != null -> {
                    log.info("Delete message ${message.text}, reason ${warn.type}, from ${message.from?.info}")
                    actions.runIfNew("garbage_warn_rule", message.delete) {
                        kotbot.executeSafely(it)
                        userContextService.addStrike(message, warn.type)
                    }
                }
            }
        }
    }

    private companion object : Logger()
}

internal fun wordRegex(word: String) = Regex("(?i)\\b$word\\b")
