package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Refunds a successful payment in [Telegram Stars](https://t.me/BotNews/90). Returns *True* on success.
 */
@Serializable
public data class RefundStarPayment(
    /**
     * Identifier of the user whose payment will be refunded
     */
    public val user_id: Long,
    /**
     * Telegram payment identifier
     */
    public val telegram_payment_charge_id: String,
) : Method<RefundStarPayment, Boolean> by Companion {
    public companion object : Method<RefundStarPayment, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<RefundStarPayment> = serializer()

        override val _name: String = "refundStarPayment"
    }
}
