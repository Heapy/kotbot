package io.heapy.kotbot.infra.jdbc

import org.jooq.DSLContext

sealed interface TransactionContext

internal data class JooqTransactionContext(
    internal val dslContext: DSLContext,
) : TransactionContext

data object MockTransactionContext : TransactionContext

context(transactionContext: TransactionContext)
suspend fun <T> useTx(
    block: suspend DSLContext.() -> T,
): T {
    return when (transactionContext) {
        is JooqTransactionContext -> block(transactionContext.dslContext)
        MockTransactionContext -> error("useTx shouldn't be called with mock transaction")
    }
}

inline val DSLContext.dslContext: DSLContext
    get() = this
