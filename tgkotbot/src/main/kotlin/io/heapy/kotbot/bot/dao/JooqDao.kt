package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.infra.Loom
import io.heapy.kotbot.infra.jdbc.JooqTransactionContext
import io.heapy.kotbot.infra.jdbc.TransactionContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jooq.DSLContext

interface JooqDao {
    suspend fun <T> transaction(
        block: suspend TransactionContext.() -> T,
    ): T
}

data class DefaultJooqDao(
    private val dslContext: DSLContext,
) : JooqDao {
    override suspend fun <T> transaction(
        block: suspend TransactionContext.() -> T,
    ): T {
        return withContext(Dispatchers.Loom) {
            val transactionContext = JooqTransactionContext(dslContext)
            block(transactionContext)
        }
    }
}
