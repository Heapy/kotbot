package io.heapy.kotbot.bot.use_case.callback

import io.heapy.kotbot.bot.KotbotCallBackData
import io.heapy.kotbot.bot.dao.CallbackDataDao
import io.heapy.kotbot.infra.jdbc.TransactionContext
import kotlinx.serialization.json.Json
import java.util.UUID

class CallbackDataService(
    private val callbackDataDao: CallbackDataDao,
    private val json: Json,
) {
    context(_: TransactionContext)
    suspend fun getById(
        id: String,
    ): KotbotCallBackData? {
        val callbackData = callbackDataDao
            .getOnceById(UUID.fromString(id))

        return callbackData?.let {
            json.decodeFromString(KotbotCallBackData.serializer(), callbackData.data.data())
        }
    }

    context(_: TransactionContext)
    suspend fun <T : KotbotCallBackData> insert(
        data: T,
    ): String? {
        val jsonData = json.encodeToString(KotbotCallBackData.serializer(), data)

        return callbackDataDao
            .insert(
                jsonData = jsonData,
            )
            ?.toString()
    }
}
