package io.heapy.kotbot.infra.jdbc

import io.heapy.kotbot.infra.Loom
import kotlinx.coroutines.*
import org.jooq.DSLContext

class TransactionProvider(
    private val dslContext: DSLContext,
) {
    suspend fun <R> transaction(
        body: suspend TransactionContext.() -> R,
    ): R {
        return withContext(Dispatchers.Loom) {
            dslContext.transactionResult { configuration ->
                runBlocking {
                    JooqTransactionContext(configuration.dsl()).body()
                }
            }
        }
    }
}
