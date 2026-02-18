package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.tables.references.UPDATE_RAW
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import org.jooq.JSONB
import java.time.LocalDateTime.now

class UpdateDao {
    context(_: TransactionContext)
    suspend fun saveRawUpdate(update: String) = useTx {
        insertInto(UPDATE_RAW)
            .set(UPDATE_RAW.CREATED, now())
            .set(UPDATE_RAW.UPDATE, JSONB.valueOf(update))
            .execute()
    }
}
