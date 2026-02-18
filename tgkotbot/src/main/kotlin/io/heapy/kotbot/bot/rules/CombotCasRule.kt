package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.TypedUpdate
import io.heapy.kotbot.bot.anyMessage
import io.heapy.kotbot.bot.banFrom
import io.heapy.kotbot.bot.delete
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.refLog
import io.heapy.kotbot.infra.configuration.CasConfiguration
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

class CombotCasRule(
    private val client: HttpClient,
    private val casConfiguration: CasConfiguration,
) : Rule {
    context(_: TransactionContext)
    override suspend fun validate(
        kotbot: Kotbot,
        update: TypedUpdate,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            val userId = message.from!!.id
            if (!casConfiguration.allowlist.contains(userId)) {
                val response = client.get("https://api.cas.chat/check?user_id=$userId").body<CasResponse>()
                if (response.ok) {
                    log.info("User ${message.from?.refLog} is CAS banned")
                    actions.runIfNew("cas_rule", message.delete) {
                        kotbot.executeSafely(it)
                    }
                    actions.runIfNew("cas_rule", message.banFrom) {
                        kotbot.executeSafely(it)
                    }
                } else {
                    log.info("User ${message.from?.refLog} is NOT CAS banned")
                }
            } else {
                log.info("User ${message.from?.refLog} is in CAS allowlist")
            }
        }
    }

    @Serializable
    private data class CasResponse(
        val ok: Boolean,
    )

    private companion object : Logger()
}
