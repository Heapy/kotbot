package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.tables.references.UPDATE_RAW
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import org.jooq.JSONB
import org.jooq.impl.DSL
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class UserStats(
    val firstSeen: LocalDateTime?,
    val messageCount: Int,
)

class UpdateDao {
    context(_: TransactionContext)
    suspend fun saveRawUpdate(update: String) = useTx {
        dslContext
            .insertInto(UPDATE_RAW)
            .set(UPDATE_RAW.CREATED, now())
            .set(UPDATE_RAW.UPDATE, JSONB.valueOf(update))
            .execute()
    }

    /**
     * Queries update_raw JSONB to compute first seen date and message count for a user.
     *
     * Each row in update_raw stores the full Telegram getUpdates response:
     * `{"ok":true,"result":[{"message":{"from":{"id":...},...},"update_id":...}]}`
     *
     * We unnest the result array, then check each element's message.from.id.
     */
    context(_: TransactionContext)
    suspend fun getUserStatsFromRaw(telegramId: Long): UserStats = useTx {
        val result = dslContext
            .select(
                DSL.min(DSL.field("ur.created", LocalDateTime::class.java)).`as`("first_seen"),
                DSL.count().`as`("msg_count"),
            )
            .from(
                DSL.table(
                    """
                    (SELECT sub.created, elem
                     FROM (SELECT ur.created, ur.update -> 'result' AS result_arr
                           FROM update_raw ur
                           WHERE jsonb_typeof(ur.update -> 'result') = 'array') sub,
                     jsonb_array_elements(sub.result_arr) AS elem
                     WHERE elem -> 'message' -> 'from' ->> 'id' = {0}) AS ur
                    """.trimIndent(),
                    DSL.`val`(telegramId.toString()),
                ),
            )
            .fetchOne()

        UserStats(
            firstSeen = result?.get("first_seen", LocalDateTime::class.java),
            messageCount = result?.get("msg_count", Int::class.java) ?: 0,
        )
    }
}
