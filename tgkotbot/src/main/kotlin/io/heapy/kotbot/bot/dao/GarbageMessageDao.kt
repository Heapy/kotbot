package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.tables.pojos.GarbageMessages
import io.heapy.kotbot.database.tables.references.GARBAGE_MESSAGES
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.dslContext
import io.heapy.kotbot.infra.jdbc.useTx

class GarbageMessageDao {
    context(_: TransactionContext)
    suspend fun getGarbageMessages(): List<GarbageMessages> = useTx {
        dslContext
            .select(
                GARBAGE_MESSAGES.ID,
                GARBAGE_MESSAGES.TEXT,
                GARBAGE_MESSAGES.TYPE,
                GARBAGE_MESSAGES.ACTION,
                GARBAGE_MESSAGES.MATCH,
            )
            .from(GARBAGE_MESSAGES)
            .fetchInto(GarbageMessages::class.java)
    }
}
