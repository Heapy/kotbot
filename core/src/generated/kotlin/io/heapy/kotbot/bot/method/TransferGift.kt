package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Transfers an owned unique gift to another user. Requires the *can_transfer_and_upgrade_gifts* business bot right. Requires *can_transfer_stars* business bot right if the transfer is paid. Returns *True* on success.
 */
@Serializable
public data class TransferGift(
    /**
     * Unique identifier of the business connection
     */
    public val business_connection_id: String,
    /**
     * Unique identifier of the regular gift that should be transferred
     */
    public val owned_gift_id: String,
    /**
     * Unique identifier of the chat which will own the gift. The chat must be active in the last 24 hours.
     */
    public val new_owner_chat_id: Long,
    /**
     * The amount of Telegram Stars that will be paid for the transfer from the business account balance. If positive, then the *can_transfer_stars* business bot right is required.
     */
    public val star_count: Int? = null,
) : Method<TransferGift, Boolean> by Companion {
    public companion object : Method<TransferGift, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<TransferGift> = serializer()

        override val _name: String = "transferGift"
    }
}
