package io.heapy.kotbot.bot.rules

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.*
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.infra.configuration.CasConfiguration
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

class CombotCasRule(
    private val client: HttpClient,
    private val casConfiguration: CasConfiguration,
) : Rule {
    override suspend fun validate(
        kotbot: Kotbot,
        update: Update,
        actions: Actions,
    ) {
        update.anyMessage?.let { message ->
            val userId = message.from!!.id
            if (!casConfiguration.allowlist.contains(userId)) {
                val response = client.get("https://api.cas.chat/check?user_id=$userId").body<CasResponse>()
                if (response.ok) {
                    log.info("User ${message.from?.info} is CAS banned")
                    actions.runIfNew("cas_rule", message.delete) {
                        kotbot.executeSafely(it)
                    }
                    actions.runIfNew("cas_rule", message.banFrom) {
                        kotbot.executeSafely(it)
                    }
                } else {
                    log.info("User ${message.from?.info} is NOT CAS banned")
                }
            } else {
                log.info("User ${message.from?.info} is in CAS allowlist")
            }
        }
    }

    @Serializable
    private data class CasResponse(
        val ok: Boolean,
    )

    private companion object : Logger()
}
