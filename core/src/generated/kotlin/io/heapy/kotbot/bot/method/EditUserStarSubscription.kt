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
 * Allows the bot to cancel or re-enable extension of a subscription paid in Telegram Stars. Returns *True* on success.
 */
@Serializable
public data class EditUserStarSubscription(
    /**
     * Identifier of the user whose subscription will be edited
     */
    public val user_id: Long,
    /**
     * Telegram payment identifier for the subscription
     */
    public val telegram_payment_charge_id: String,
    /**
     * Pass *True* to cancel extension of the user subscription; the subscription must be active up to the end of the current subscription period. Pass *False* to allow the user to re-enable a subscription that was previously canceled by the bot.
     */
    public val is_canceled: Boolean,
) : Method<EditUserStarSubscription, Boolean> by Companion {
    public companion object : Method<EditUserStarSubscription, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<EditUserStarSubscription> = serializer()

        override val _name: String = "editUserStarSubscription"
    }
}
