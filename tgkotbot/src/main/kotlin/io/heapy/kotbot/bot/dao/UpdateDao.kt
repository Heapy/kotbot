package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.tables.references.UPDATE_RAW
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import org.jooq.JSONB
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
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
     * Computes first-seen date and message count for ALL users in a single pass over
     * update_raw, instead of one full scan per user.
     *
     * Each row stores the full Telegram getUpdates response:
     * `{"ok":true,"result":[{"message":{"from":{"id":...},...},"update_id":...}]}`
     *
     * We keep rows whose result is a JSON array, unnest it once, keep elements that
     * carry message.from.id, and GROUP BY that id. Returned map is keyed by telegramId.
     */
    context(_: TransactionContext)
    suspend fun getAllUserStatsFromRaw(): Map<Long, UserStats> = useTx {
        // update -> 'result'  (typed jOOQ JSONB navigation)
        val resultArr = DSL.jsonbGetAttribute(UPDATE_RAW.UPDATE, "result")

        // inner derived table: only rows whose result is a JSON array
        val sub = dslContext
            .select(
                UPDATE_RAW.CREATED,
                resultArr.`as`("result_arr"),
            )
            .from(UPDATE_RAW)
            .where(DSL.function("jsonb_typeof", SQLDataType.VARCHAR, resultArr).eq("array"))
            .asTable("sub")

        val subCreated = sub.field(UPDATE_RAW.CREATED)
        val subResultArr = sub.field("result_arr", JSONB::class.java)

        // unnest: one row per array element (irreducible set-returning function)
        val elements = DSL.table("jsonb_array_elements({0})", subResultArr).`as`("elem", "value")
        val elem = DSL.field(DSL.name("elem", "value"), JSONB::class.java)

        // elem -> 'message' -> 'from' ->> 'id'  (typed navigation), cast to bigint
        val fromObj = DSL.jsonbGetAttribute(DSL.jsonbGetAttribute(elem, "message"), "from")
        val fromIdText = DSL.jsonbGetAttributeAsText(fromObj, "id")
        val telegramId = fromIdText.cast(SQLDataType.BIGINT)

        dslContext
            .select(
                telegramId.`as`("telegram_id"),
                DSL.min(subCreated).`as`("first_seen"),
                DSL.count().`as`("msg_count"),
            )
            .from(sub)
            .crossJoin(elements) // function in FROM ⇒ PostgreSQL applies implicit LATERAL
            .where(fromIdText.isNotNull)
            // group by the SELECT alias, not the repeated cast expression: PostgreSQL won't
            // match a complex expression between SELECT and GROUP BY, but resolves the alias.
            .groupBy(DSL.field(DSL.name("telegram_id")))
            .fetch()
            .associate { record ->
                record.get("telegram_id", Long::class.java) to UserStats(
                    firstSeen = record.get("first_seen", LocalDateTime::class.java),
                    messageCount = record.get("msg_count", Int::class.java) ?: 0,
                )
            }
    }

    /**
     * Fetches up to [limit] message texts for the given user from UPDATE_RAW JSONB.
     * Messages are ordered by the row creation timestamp.
     */
    context(_: TransactionContext)
    suspend fun getUserMessageTexts(telegramId: Long): List<String> = useTx {
        dslContext
            .fetch(
                """
                SELECT elem -> 'message' ->> 'text' AS text
                FROM (
                    SELECT ur.created, ur.update -> 'result' AS result_arr
                    FROM update_raw ur
                    WHERE jsonb_typeof(ur.update -> 'result') = 'array'
                ) sub,
                jsonb_array_elements(sub.result_arr) AS elem
                WHERE elem -> 'message' -> 'from' ->> 'id' = {0}
                  AND elem -> 'message' ->> 'text' IS NOT NULL
                  AND trim(elem -> 'message' ->> 'text') <> ''
                ORDER BY sub.created
                """.trimIndent(),
                DSL.`val`(telegramId.toString()),
            )
            .mapNotNull { it.get("text", String::class.java) }
    }
}
