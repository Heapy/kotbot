package io.heapy.kotbot.infra.jdbc

import org.jooq.DSLContext

sealed interface TransactionContext

internal data class JooqTransactionContext(
    internal val dslContext: DSLContext,
) : TransactionContext

suspend fun <T> TransactionContext.useTx(
    block: suspend DSLContext.() -> T,
): T {
    return when (val context = this) {
        is JooqTransactionContext -> block(context.dslContext)
    }
}

val DSLContext.dslContext: DSLContext
    get() = this
