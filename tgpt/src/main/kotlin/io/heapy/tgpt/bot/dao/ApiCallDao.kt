package io.heapy.tgpt.bot.dao

import io.heapy.tgpt.database.tables.ApiCall.Companion.API_CALL
import io.heapy.tgpt.database.tables.AllowedUser.Companion.ALLOWED_USER
import io.heapy.tgpt.infra.jdbc.TransactionContext
import io.heapy.tgpt.infra.jdbc.useTx
import org.jooq.impl.DSL
import java.math.BigDecimal

class ApiCallDao {
    context(tx: TransactionContext)
    suspend fun recordApiCall(
        threadId: Long,
        telegramUserId: Long,
        model: String,
        promptTokens: Int,
        completionTokens: Int,
        totalTokens: Int,
        estimatedCostUsd: BigDecimal,
    ) = useTx {
        dslContext.insertInto(API_CALL)
            .set(API_CALL.THREAD_ID, threadId)
            .set(API_CALL.TELEGRAM_USER_ID, telegramUserId)
            .set(API_CALL.MODEL, model)
            .set(API_CALL.PROMPT_TOKENS, promptTokens)
            .set(API_CALL.COMPLETION_TOKENS, completionTokens)
            .set(API_CALL.TOTAL_TOKENS, totalTokens)
            .set(API_CALL.ESTIMATED_COST_USD, estimatedCostUsd)
            .execute()
    }

    context(tx: TransactionContext)
    suspend fun totalSpend(): BigDecimal = useTx {
        dslContext.select(DSL.sum(API_CALL.ESTIMATED_COST_USD))
            .from(API_CALL)
            .fetchOne()
            ?.get(0, BigDecimal::class.java)
            ?: BigDecimal.ZERO
    }

    context(tx: TransactionContext)
    suspend fun totalApiCalls(): Int = useTx {
        dslContext.fetchCount(API_CALL)
    }

    data class UserStats(
        val telegramUserId: Long,
        val username: String?,
        val promptTokens: Long,
        val completionTokens: Long,
        val totalTokens: Long,
        val costUsd: BigDecimal,
        val threads: Long,
    )

    context(tx: TransactionContext)
    suspend fun perUserStats(): List<UserStats> = useTx {
        dslContext
            .select(
                API_CALL.TELEGRAM_USER_ID,
                ALLOWED_USER.USERNAME,
                DSL.sum(API_CALL.PROMPT_TOKENS).`as`("prompt_tokens"),
                DSL.sum(API_CALL.COMPLETION_TOKENS).`as`("completion_tokens"),
                DSL.sum(API_CALL.TOTAL_TOKENS).`as`("total_tokens"),
                DSL.sum(API_CALL.ESTIMATED_COST_USD).`as`("cost_usd"),
                DSL.countDistinct(API_CALL.THREAD_ID).`as`("threads"),
            )
            .from(API_CALL)
            .leftJoin(ALLOWED_USER).on(API_CALL.TELEGRAM_USER_ID.eq(ALLOWED_USER.TELEGRAM_ID))
            .groupBy(API_CALL.TELEGRAM_USER_ID, ALLOWED_USER.USERNAME)
            .orderBy(DSL.field("cost_usd").desc())
            .fetch { record ->
                UserStats(
                    telegramUserId = record.get(API_CALL.TELEGRAM_USER_ID)!!,
                    username = record.get(ALLOWED_USER.USERNAME),
                    promptTokens = record.get("prompt_tokens", Long::class.java) ?: 0L,
                    completionTokens = record.get("completion_tokens", Long::class.java) ?: 0L,
                    totalTokens = record.get("total_tokens", Long::class.java) ?: 0L,
                    costUsd = record.get("cost_usd", BigDecimal::class.java) ?: BigDecimal.ZERO,
                    threads = record.get("threads", Long::class.java) ?: 0L,
                )
            }
    }
}
