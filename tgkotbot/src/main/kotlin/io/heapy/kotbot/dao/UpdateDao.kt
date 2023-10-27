package io.heapy.kotbot.dao

import io.heapy.kotbot.database.tables.references.UPDATE_RAW
import org.jooq.DSLContext
import org.jooq.JSONB
import java.time.LocalDateTime.now

class UpdateDao {
    context(DSLContext)
    fun saveRawUpdate(update: String) {
        insertInto(UPDATE_RAW)
            .set(UPDATE_RAW.CREATED, now())
            .set(UPDATE_RAW.UPDATE, JSONB.valueOf(update))
            .execute()
    }
}
