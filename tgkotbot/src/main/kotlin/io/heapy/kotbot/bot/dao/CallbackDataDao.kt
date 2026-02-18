package io.heapy.kotbot.bot.dao

import io.heapy.kotbot.database.tables.CallbackData.Companion.CALLBACK_DATA
import io.heapy.kotbot.database.tables.pojos.CallbackData
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.jdbc.useTx
import org.jooq.JSONB
import java.time.LocalDateTime
import java.util.*

class CallbackDataDao {
    context(_: TransactionContext)
    suspend fun getOnceById(
        id: UUID,
    ): CallbackData? = useTx {
        dslContext
            .update(CALLBACK_DATA)
            .set(CALLBACK_DATA.CONSUMED, true)
            .where(CALLBACK_DATA.ID.eq(id))
            .and(CALLBACK_DATA.CONSUMED.eq(false))
            .returning()
            .fetchOneInto(CallbackData::class.java)
    }

    context(_: TransactionContext)
    suspend fun insert(
        jsonData: String,
    ): UUID? = useTx {
        dslContext
            .insertInto(
                CALLBACK_DATA,
                CALLBACK_DATA.DATA,
                CALLBACK_DATA.CREATED,
            )
            .values(
                JSONB.valueOf(jsonData),
                LocalDateTime.now(),
            )
            .returning(CALLBACK_DATA.ID)
            .fetchOne()
            ?.getValue(CALLBACK_DATA.ID)
    }
}
