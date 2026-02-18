package io.heapy.tgpt.infra.jdbc

import org.jooq.DSLContext

sealed interface TransactionContext

internal data class JooqTransactionContext(
    internal val dslContext: DSLContext,
) : TransactionContext

data object MockTransactionContext : TransactionContext

interface DSLContextHolder {
    val dslContext: DSLContext
}

private data class DefaultDSLContextHolder(
    override val dslContext: DSLContext,
) : DSLContextHolder

context(tx: TransactionContext)
suspend fun <T> useTx(
    block: suspend DSLContextHolder.() -> T,
): T {
    return when (tx) {
        is JooqTransactionContext -> block(DefaultDSLContextHolder(tx.dslContext))
        MockTransactionContext -> error("useTx shouldn't be called with mock transaction")
    }
}
