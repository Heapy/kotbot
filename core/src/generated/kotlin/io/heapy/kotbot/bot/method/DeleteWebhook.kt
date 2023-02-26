package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to remove webhook integration if you decide to switch back to [getUpdates](https://core.telegram.org/bots/api/#getupdates). Returns *True* on success.
 */
@Serializable
public data class DeleteWebhook(
    /**
     * Pass *True* to drop all pending updates
     */
    public val drop_pending_updates: Boolean? = null,
) : Method<DeleteWebhook, Boolean> by Companion {
    public companion object : Method<DeleteWebhook, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<DeleteWebhook> = serializer()

        override val _name: String = "deleteWebhook"
    }
}
