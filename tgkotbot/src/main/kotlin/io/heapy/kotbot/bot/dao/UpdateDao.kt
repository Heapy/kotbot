package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.tables.references.UPDATE_RAW
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.jooq.JSONB
import java.time.LocalDateTime.now

class UpdateDao(
    private val ioDispatcher: CoroutineDispatcher,
) {
    context(DSLContext)
    suspend fun saveRawUpdate(update: String) = withContext(ioDispatcher) {
        insertInto(UPDATE_RAW)
            .set(UPDATE_RAW.CREATED, now())
            .set(UPDATE_RAW.UPDATE, JSONB.valueOf(update))
            .execute()
    }
}
